package com.example.mychatapp.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mychatapp.Fragment.MainFragment
import com.example.mychatapp.R
import com.example.mychatapp.Service.AppService
import com.example.mychatapp.Service.NotifyService
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val mainViewModel=MainViewModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(MainFragment(this,this),R.id.MainFrame,"MainFrame")
        getFcmToken()
        startService(Intent(this,AppService::class.java))
    }

    private fun getFcmToken() {
      FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
          if (it.isSuccessful){
              val token=it.result
              if(token.isNotEmpty()) {
                  mainViewModel.makeUpdate("fcmToken",token,AppOwner(this))
              }
          }else{
              Log.d("khong co tooken","no token....")
          }
      })
    }

    private fun replaceFragment(fragment : Fragment,idFrame:Int,tag:String){
        val fragmentManager =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(idFrame,fragment,tag)
        fragmentTransaction.commit()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.makeOffLive(AppOwner(this).getUid().toString())
        Log.d("pause","pause: "+AppOwner(this).getUid().toString())
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.makeOnLive(AppOwner(this).getUid().toString())
        Log.d("Resume","Resume")
    }


}