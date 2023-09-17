package com.example.mychatapp.Ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mychatapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {
    private  var loginSuccess=false
    private val isnull=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        lifecycleScope.launch {
             delay(250)
             change()
             finish()
        }

    }


    private fun change(){
      val sharedPref = this.getSharedPreferences("Ui.LoginSignup",Context.MODE_PRIVATE)
      val result= sharedPref.getString("Uid",isnull)
      if(result!=isnull){
          loginSuccess=true
      }
        if(loginSuccess){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        if(!loginSuccess){
            val intent=Intent(this,LoginSignup::class.java)
            startActivity(intent)
        }
    }
}