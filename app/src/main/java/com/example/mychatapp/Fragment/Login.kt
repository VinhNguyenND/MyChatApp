package com.example.mychatapp.Fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mychatapp.Model.Account
import com.example.mychatapp.Ui.MainActivity
import com.example.mychatapp.R
import com.example.mychatapp.ViewModel.LoginViewModel
import com.example.mychatapp.databinding.LoginFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Login: Fragment() {
     private lateinit var binding:LoginFragmentBinding
     private val loginViewModel=LoginViewModel.getInstance()
     private var account=ArrayList<Account>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun replaceFragment(fragment: Fragment ){
        val fragmentTransaction :FragmentTransaction=parentFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.loginFrameLay,fragment)
        fragmentTransaction.addToBackStack("Login")
        fragmentTransaction.commit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtSignUp.setOnClickListener {
            val signUp= SignUp()
            replaceFragment(signUp)
        }

        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            val email=binding.edtEmail.text.toString()
            val passWord=binding.edtPassWord.text.toString()
            var yes=true
            if(email.isEmpty()){
                showError(binding.edtEmail,"vui long nhap email")
                yes=false
            }
            if(passWord.isEmpty()){
                showError(binding.edtPassWord,"vui long nhap password")
                yes=false
            }
            if(yes) {
                loginViewModel.checkLogin(email, passWord)
                    .observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            if (it.size>0) {
                                account=it
                                binding.progressBar.visibility=View.INVISIBLE
                                activity?.let { it1 ->
                                    loginViewModel.makeSaveAccount(it1,account[0])
                                    lifecycleScope.launch {
                                    delay(2000);
                                    binding.progressBar.visibility=View.GONE
                                    val intent=Intent(activity,MainActivity::class.java);
                                    startActivity(intent)
                                }
                                }
                            }else{
                                Toast.makeText(activity,"tai khoan dang nhap khong chinh xac",Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            }

        }
    }
    private fun showError(error:EditText, s:String){
        error.error=s
    }
    fun check(error:EditText){

    }


}