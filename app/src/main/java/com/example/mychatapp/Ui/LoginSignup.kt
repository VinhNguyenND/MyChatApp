package com.example.mychatapp.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mychatapp.Fragment.Login
import com.example.mychatapp.R

class LoginSignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)
        val login=Login()
        replaceFragment(login)
    }
    private fun replaceFragment(fragment :Fragment){
        val fragmentManager =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginFrameLay,fragment)
        fragmentTransaction.commit()
    }

}