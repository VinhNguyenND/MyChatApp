package com.example.mychatapp.Fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.Ui.LoginSignup
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.SettingfragmentBinding

class SettingFragment(context: Context):Fragment() {
    private lateinit var binding : SettingfragmentBinding
    private  val mainViewModel =MainViewModel.getInstance()
    private val mContext=context
    private var profile:UserModel?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=SettingfragmentBinding.inflate(inflater,container,false)
        (requireActivity() as AppCompatActivity ).supportActionBar?.hide()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeWithClick()
        makeWithData()
        makeView()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.makeOwner(AppOwner(mContext).getUid().toString())
    }
    private  fun makeWithClick(){
        binding.setImage.setOnClickListener(View.OnClickListener {
           activityResultLauncher.launch(REQUIRED_PERMISSIONS)
       })
        binding.Logout.setOnClickListener {
            startActivity()
            activity?.finish()
            logOut()
        }
    }
    private  fun startActivity(){
        val intent = Intent(mContext, LoginSignup::class.java)
        startActivity(intent)
    }
    private fun logOut(){
        val sharedPreferences = mContext.getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("TênKey")
        editor.apply()
    }
    private fun makeView(){
        binding.pfProgress.visibility=View.VISIBLE
    }
    private fun makeWithData(){
        mainViewModel.profileSetting().observe(viewLifecycleOwner, Observer {
              profile=it
              setProperty(it)
              if(binding.pfProgress.visibility==View.VISIBLE){
                  binding.pfProgress.visibility=View.GONE
              }
        })
    }
    private fun setProperty(userProfile:UserModel){
        Glide.with(mContext)
            .load(userProfile.image)
            .error(R.drawable.blank_avatar)
            .fitCenter()
            .into(binding.ownerImage)
        binding.ownerName.text=userProfile.Name

    }
    private fun replaceFragment(fragment: Fragment,id:Int){
        val fragmentManager: FragmentManager =parentFragmentManager
        val fragmentTransition: FragmentTransaction =fragmentManager.beginTransaction()
        fragmentTransition.replace(id,fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }
    private val activityResultLauncher =
    registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && it.value == false){
                permissionGranted = false
                Toast.makeText(requireContext(),it.key.toString()+" "+it.value.toString(),Toast.LENGTH_LONG).show()
            }
        }
        if (!permissionGranted) {
            Toast.makeText(requireActivity(),
                "Permission request denied",
                Toast.LENGTH_SHORT).show()

        } else {
           replaceFragment(CameraFragment(mContext),R.id.MainFrame)
        }
    }
     companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}