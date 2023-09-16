package com.example.mychatapp.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.mychatapp.Model.ChatListModel
import com.example.mychatapp.Model.Invited
import com.example.mychatapp.Model.MessageModel
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.Repository.AppRepository
import com.example.mychatapp.Util.AppOwner
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    private var repository:AppRepository = AppRepository.getInstance()
    private var chatsLiveData= MutableLiveData<ArrayList<ChatListModel>>()
    private var userLiveData= MutableLiveData<ArrayList<UserModel>>()
    private var isInvited= MutableLiveData<ArrayList<Invited>>()
    private var idUser=MutableLiveData<String>()
    private val isFriend=MutableLiveData<Boolean>()
    private val mProfile=MutableLiveData<UserModel>()
    private val mFriendProfile=MutableLiveData<UserModel>()
    private var mChatExist=MutableLiveData<ChatListModel?>()
    private val mRequest=MutableLiveData<ArrayList<UserModel>>()

   fun getChats(id:String):LiveData<ArrayList<ChatListModel>>{
        viewModelScope.launch {
        repository.fetchChats(id)
       }
       return chatsLiveData
   }
    fun getUid():LiveData<String>{
        return  idUser
    }
    fun getInvited():LiveData<ArrayList<Invited>>{
        return isInvited
    }
    fun getFriend():LiveData<Boolean>{
        return isFriend
    }
    fun getRequest():LiveData<ArrayList<UserModel>>{
        return mRequest
    }
//    fun getFriendProfile():LiveData<UserModel>{
//        return mFriendProfile
//    }
    fun getChatExist():LiveData<ChatListModel?>{
        return  mChatExist
    }
    init {
       repository.getIsInvited().observeForever(Observer{
          isInvited.value=it
        })
        repository.getChats().observeForever(Observer {
           chatsLiveData.value=it
       })
       repository.getUser().observeForever(Observer {
           userLiveData.value=it
       })
        repository.getUserId().observeForever(Observer {
            idUser.value=it
        })
        repository.getIsFriend().observeForever(Observer {
            isFriend.value=it
        })
        repository.getRequest().observeForever(Observer {
            mRequest.value=it
        })
        repository.getFriendProfile().observeForever(Observer {
            mFriendProfile.value=it
        })
        repository.getChatExist().observeForever(Observer {
            mChatExist.value=it
        })
    }

    companion object{
       @Volatile private var  instance:MainViewModel?=null
         fun getInstance()= instance ?: synchronized(this){
             instance?:MainViewModel().also { instance=it }
         }
    }

    fun getUser():LiveData<ArrayList<UserModel>>{
        return userLiveData
    }

     fun findUser(mark:String):LiveData<ArrayList<UserModel>>{
        repository.findUser(mark)
        return userLiveData
    }

    fun getFriendProfile():LiveData<UserModel>{
        return  mFriendProfile
    }

    fun createCom(chatListModel: ChatListModel){
       repository.createCom(chatListModel)
    }

    fun makePerOnline(id:String){
        repository.getFriendOnline(id)
    }

    fun makeCheckInvited(idOne:String,idTwo:String){
        repository.checkInvited(idOne, idTwo)
    }

    fun makeCheckFriend(idYourSelf:String,userModel: UserModel){
     repository.checkFriend(idYourSelf,userModel)
    }
    fun getFriend(idYourSelf:String){
       repository.getFriend(idYourSelf)
    }
    fun makeUid(Email:String){
        repository.getId(Email)
    }
    fun blockFriend(idYourSelf:String,idFriend:String,myEmail:String,userModel: UserModel){
        repository.cancelFriend(idYourSelf,idFriend,myEmail, userModel)
    }
    fun acceptFriend(idYourSelf:String,idFriend:String,myEmail:String,userModel: UserModel){
        repository.addFriend(idYourSelf,idFriend,myEmail,userModel)
    }
    fun acceptFriend(idYourSelf:String,myEmail: String,Email:String){
        repository.acceptFriend(idYourSelf, myEmail, Email)
    }
    fun requestFriend(idOne:String,idTwo:String){
        repository.requestFriend(idOne, idTwo)
    }
    fun deleteRequest(idOne: String, idTwo: String){
        repository.deleteRequest(idOne, idTwo)
    }
    fun deleteRequest1(idOne: String,Email: String){
        repository.deleteRequest1(idOne, Email)
    }
    fun profileSetting():LiveData<UserModel>{
        repository.getProfile().observeForever(Observer {
            mProfile.value=it
        })
        return  mProfile
    }
    fun makeOwner(id: String){
        repository.getProfile(id)
    }
    fun makeOffLive(id:String){
        repository.offLive(id)
    }
    fun makeOnLive(id: String){
        repository.onLive(id)
    }
    fun <T> makeUpdate(key:String,value:T,App: AppOwner){
        repository.update(key, value, App)
    }
    fun setImage(imageUri: ByteArray, id:String,time:String){
        repository.setImage(imageUri,time, id)
    }
    fun getRequest(id:String,context: Context){
        repository.getRequest(id,context)
    }
    fun clearInvited(){
        repository.clearInvited()
    }
    fun checkChatExist(idOne:String,idTwo:String){
        repository.findChatExist(idOne, idTwo)
    }


}