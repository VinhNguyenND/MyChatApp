package com.example.mychatapp.Fragment

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.ViewModel.LoginViewModel
import com.example.mychatapp.databinding.SignupFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp: Fragment() {
    private lateinit var binding: SignupFragmentBinding;
    private val loginViewModel= LoginViewModel.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding=SignupFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtLogin.setOnClickListener {
           activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
           loginViewModel.addAccount(binding.edtEmail.text.toString(),binding.edtPassWord.text.toString(),binding.edtName.text.toString())
        }
    }

}