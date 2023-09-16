package com.example.mychatapp.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.ImageFragmentBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ImageFragment(context: Context):Fragment() {
    private  lateinit var binding:ImageFragmentBinding
    private val mContext=context
    private val mainViewModel=MainViewModel.getInstance()
    private var receivedBitmap:Bitmap?=null

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=ImageFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeWithClick()
        val receivedBundle = arguments;
        if (receivedBundle != null) {
           receivedBitmap = receivedBundle.getParcelable("bitmapKey");
            if (receivedBitmap != null) {
                 binding.imgPrePro.let{
                     it.setImageBitmap(receivedBitmap)
                     it.scaleType=ImageView.ScaleType.FIT_XY
                 }
            }
        }
    }
    private fun makeWithClick(){
        binding.txtChupLai.setOnClickListener(View.OnClickListener {
            val fragmentManager:FragmentManager=parentFragmentManager
            fragmentManager.popBackStack()
        })
        binding.txtChapNhan.setOnClickListener (View.OnClickListener {
            upload(receivedBitmap)
            val fragmentManager:FragmentManager=parentFragmentManager
            fragmentManager.popBackStack()
            fragmentManager.popBackStack()
        })
    }
    @SuppressLint("Recycle")
    private fun upload(it: Bitmap?) {
        val baos = ByteArrayOutputStream()
        it?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val image_fb = baos.toByteArray()
        mainViewModel.setImage(image_fb,System.currentTimeMillis().toString(),AppOwner(mContext).getUid().toString())
    }


}