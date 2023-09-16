package com.example.mychatapp.Fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.databinding.CameraFragmentBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.mychatapp.R
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit
class CameraFragment(context:Context):Fragment() {
    private lateinit var binding: CameraFragmentBinding
    private  val mContext=context
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private var flash:Boolean=true
    private  var  cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var camera:Camera
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=CameraFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        makeWithClick()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.isShutdown
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            /* Used to bind the lifecycle of cameras to the lifecycle owner*/
            cameraProvider = cameraProviderFuture.get()
            /* Preview*/
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            //
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    }) }
            /* Select back camera as a default*/

            try {
                /* Unbind use cases before rebinding*/
                cameraProvider.unbindAll()
                /* Bind use cases to camera*/
                 camera= cameraProvider.bindToLifecycle(
               this, cameraSelector, preview, imageCapture, imageAnalyzer)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            } }, ContextCompat.getMainExecutor(requireContext())) }

    private fun makeWithClick(){
        binding.btnHuy.setOnClickListener {
             val fragmentManager: FragmentManager =parentFragmentManager
             fragmentManager.popBackStack()
        }
        binding.buttonCapture.setOnClickListener(View.OnClickListener {
             takePhoto()
        })
        binding.wrapCamera.setOnClickListener(View.OnClickListener {
         if(cameraSelector== CameraSelector.DEFAULT_BACK_CAMERA){
             cameraSelector=CameraSelector.DEFAULT_FRONT_CAMERA
         }else{
             cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA
         }
         startCamera()
        })

    }
    private fun takePhoto() {
        /* Get a stable reference of the modifiable image capture use case*/
        val imageCapture = imageCapture ?: return
        /* Create time stamped name and MediaStore entry.*/
        val fileNameFormat:String="EEE, MMM d, ''yy"
        val name = SimpleDateFormat(fileNameFormat, Locale.US).format(System.currentTimeMillis());
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
        /* Create output options object which contains file + metadata*/
           val outputOptions = ImageCapture.OutputFileOptions
           .Builder(requireActivity().contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
           .build()
        // Set up image capture listener, which is triggered after photo has
            /* been taken*/

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()),@ExperimentalGetImage object :ImageCapture.OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                 val bitmap = image.image?.toBitmap(image);
                if (bitmap != null) {
                    passData(ImageFragment(mContext),R.id.MainFrame,bitmap)
                }
                 image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        })
    }
    private class LuminosityAnalyzer(private val listener: LumaListener ) : ImageAnalysis.Analyzer {
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind();    /* Rewind the buffer to zero*/
            val data = ByteArray(remaining());
            get(data);   /* Copy the buffer into a byte array*/
            return data /* Return the byte array*/
        }
        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer;
            val data = buffer.toByteArray();
            val pixels = data.map { it.toInt() and 0xFF };
            val luma = pixels.average();
            listener(luma);
            image.close()
   } }

    fun Image.toBitmap(image: ImageProxy): Bitmap? {
        val buffer = image.planes[0].buffer;
        val bytes = ByteArray(buffer.capacity());
        buffer.get(bytes);
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size);
        val matrix = Matrix();
        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private  fun passData(fragment:Fragment,id:Int,bitmap:Bitmap){
        val bundle=Bundle()
        bundle.putParcelable("bitmapKey",bitmap)
        fragment.arguments=bundle
        val fragmentManager:FragmentManager=parentFragmentManager
        val fragmentTransition: FragmentTransaction =fragmentManager.beginTransaction()
        fragmentTransition.replace(id,fragment)
        fragmentTransition.addToBackStack("camera")
        fragmentTransition.commit()
    }


}