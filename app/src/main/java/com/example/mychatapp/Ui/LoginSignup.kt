package com.example.mychatapp.Ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mychatapp.Fragment.Login
import com.example.mychatapp.R

class LoginSignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)
        deleteAccount(getDeleteAccount())
        val login=Login()
        replaceFragment(login)

    }
    private fun replaceFragment(fragment :Fragment){
        val fragmentManager =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginFrameLay,fragment)
        fragmentTransaction.commit()
    }
    private fun getDeleteAccount():String?{
        val receivedBundle = intent.extras
        var delete:String?=null
        if(receivedBundle!=null){
            val value1 = receivedBundle.getString("delete")
            delete=value1
        }
        return delete
    }
    private fun deleteAccount(intent:String?){
        if(intent=="delete"){
            val sharedPreferences = getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE);
            val editor = sharedPreferences.edit();
            editor.putString("Uid","");
            editor.putString("Email","");
            editor.apply()
        }
    }


}