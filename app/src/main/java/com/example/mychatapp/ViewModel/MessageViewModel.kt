package com.example.mychatapp.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.mychatapp.Model.MessageModel
import com.example.mychatapp.Repository.AppRepository
import com.example.mychatapp.Util.AppOwner
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class MessageViewModel: ViewModel() {
    private val mRepository=AppRepository.getInstance()
    private val messageLiveData=MutableLiveData<ArrayList<MessageModel>>()
    companion object{
       @Volatile private var  instance:MessageViewModel?=null
         fun getInstance()= instance ?: synchronized(this){
             instance?:MessageViewModel().also { instance=it }
         }
    }
    init {
        loadData()
    }
    fun getMess():LiveData<ArrayList< MessageModel>>{
        return messageLiveData
    }
   private fun loadData(){
       mRepository.getMess().observeForever{
           messageLiveData.value=it
       }
   }
    fun sendMess(messageModel: MessageModel,context: Context){
        viewModelScope.launch {
            mRepository.sendMessage(messageModel,context)
        }
    }

     fun getMessById(documentId: String){
      viewModelScope.launch {
          mRepository.fetchMessById(documentId)
      }
    }
    fun sendNoty(App: AppOwner, toUser:String, message:String){
       mRepository.sendNotificationToUser(App, toUser, message)
    }

}