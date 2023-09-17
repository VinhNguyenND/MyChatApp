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
import androidx.lifecycle.lifecycleScope
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.ViewModel.LoginViewModel
import com.example.mychatapp.databinding.SignupFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUp: Fragment() {
    private lateinit var binding: SignupFragmentBinding;
    private val loginViewModel= LoginViewModel.getInstance()
    private var isSuccess=false
    private var isExist=false
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
        data()
        action()
    }
    private fun data(){
        loginViewModel.signSuccess().observe(viewLifecycleOwner, Observer {
            isSuccess=it
            if(isSuccess) {
                binding.prSignUp.visibility = View.GONE;
                Toast.makeText(requireContext(),"Đăng ký Thành Công",Toast.LENGTH_LONG).show()

            }else {
                binding.prSignUp.visibility = View.GONE
                Toast.makeText(requireContext(),"Đăng ký thất bại",Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun action(){
        binding.txtLogin.setOnClickListener {
           activity?.supportFragmentManager?.popBackStack()
        }
        loginViewModel.userExist().observe(viewLifecycleOwner, Observer {
                isExist=it
                if(isExist!=true){
                 loginViewModel.addAccount(binding.edtEmail.text.toString().trim(),binding.edtPassWord.text.toString().trim(),binding.edtName.text.toString().trim())
                }else {
                 Toast.makeText(requireContext(),"Tài Khoản Đã Tồn Tại",Toast.LENGTH_LONG).show()
                 binding.prSignUp.visibility = View.GONE
                }
                Log.d("lay account fragment",it.toString())
                binding.prSignUp.visibility = View.GONE
            })
        binding.btnSignUp.setOnClickListener {
            if(!checkNull()) {
                if(checkPassWord()) {
                    binding.prSignUp.visibility = View.VISIBLE
                    val Email = binding.edtEmail.text.toString().trim();
                    loginViewModel.checkSignUp(Email)
                    binding.edtEmail.text = null
                    binding.edtName.text = null
                    binding.edtConfirm.text = null
                    binding.edtPassWord.text = null
                }else{
                 Toast.makeText(requireContext(),"Password Không Khớp",Toast.LENGTH_LONG).show()
                }
            }else{
                 Toast.makeText(requireContext(),"Vui Lòng Nhập Đủ Thông Tin",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checkNull():Boolean{
        val a= listOf<String>(
             binding.edtEmail.text.toString().trim()
            ,binding.edtPassWord.text.toString().trim()
            ,binding.edtName.text.toString().trim()
            ,binding.edtConfirm.text.toString().trim())
        var isEmpty=false
        for(i in a.indices){
            if(a[i].toString().isEmpty()){
                isEmpty=true
            }
        }
        return isEmpty
    }
    private fun checkPassWord():Boolean{
        return binding.edtPassWord.text.toString().trim()==binding.edtConfirm.text.toString().trim()
    }


}