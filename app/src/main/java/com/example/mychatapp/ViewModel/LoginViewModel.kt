package com.example.mychatapp.ViewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mychatapp.Model.Account
import com.example.mychatapp.Repository.AppRepository

class LoginViewModel: ViewModel() {
    private var  mrepository:AppRepository = AppRepository.getInstance()
    private  var isAccount=MutableLiveData<ArrayList<Account>?>()

    fun checkSignUp(Email:String){
       mrepository.checkUserSignUp(Email)
    }
    fun addAccount(Email: String,passWord: String,Name:String){
       mrepository.addAccount(Email,passWord,Name)
    }
   init {
       loadData()
   }

   private fun loadData(){
     mrepository.getAccount().observeForever{
         isAccount.value=it
      }
   }
    fun checkLogin(Email: String, passWord: String):LiveData<ArrayList<Account>?>{
        mrepository.checkUser(Email,passWord)
        return isAccount
    }
    fun makeSaveAccount(activity: Activity, account:Account){
        mrepository.saveAccount(activity,account)
    }
    companion object{
       @Volatile private var  instance:LoginViewModel?=null
         fun getInstance()= instance ?: synchronized(this){
             instance?:LoginViewModel().also { instance=it }
         }
    }
}


