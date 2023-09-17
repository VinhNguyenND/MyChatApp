package com.example.mychatapp.Repository


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mychatapp.Model.*
import com.example.mychatapp.Util.AppOwner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.Collections
import kotlin.math.E


class AppRepository() {
    private val userLiveData = MutableLiveData<ArrayList<UserModel>>()
    private val userSearch = MutableLiveData<ArrayList<UserModel>>()
    private val isAccount = MutableLiveData<ArrayList<Account>?>()
    private val already = MutableLiveData<Boolean>()
    private val chatsLiveData = MutableLiveData<ArrayList<ChatListModel>>()
    private val chatExist = MutableLiveData<ChatListModel?>()
    private val messageLiveData = MutableLiveData<ArrayList<MessageModel>>()
    private val isInvited = MutableLiveData<ArrayList<Invited>>()
    private val isFriend = MutableLiveData<Boolean>()
    private val signUpSuccess=MutableLiveData<Boolean>()
    private val profile = MutableLiveData<UserModel?>()
    private val frProfile = MutableLiveData<UserModel>()
    private val idUser = MutableLiveData<String>()
    private var request = MutableLiveData<ArrayList<UserModel>>()
    private val db = Firebase.firestore
    private var storageRef = Firebase.storage.reference


    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppRepository().also {
                instance = it
            }
        }
    }

    fun getUser(): LiveData<ArrayList<UserModel>> {
        return userLiveData
    }

    fun getAccount(): LiveData<ArrayList<Account>?> {
        return isAccount
    }

    fun getChats(): LiveData<ArrayList<ChatListModel>> {
        return chatsLiveData
    }

    fun getMess(): LiveData<ArrayList<MessageModel>> {
        return messageLiveData
    }

    fun getUserSearch(): LiveData<ArrayList<UserModel>> {
        return userSearch
    }

    fun getIsInvited(): LiveData<ArrayList<Invited>> {
        return isInvited
    }

    fun getUserId(): LiveData<String> {
        return idUser
    }

    fun getIsFriend(): LiveData<Boolean> {
        return isFriend
    }

    fun getProfile(): LiveData<UserModel?> {
        return profile
    }

    fun getFriendProfile(): LiveData<UserModel?> {
        return frProfile
    }

    fun getRequest(): LiveData<ArrayList<UserModel>> {
        return request
    }

    fun getChatExist(): LiveData<ChatListModel?> {
        return chatExist
    }
    fun getSignUpSuccess(): LiveData<Boolean>{
        return signUpSuccess
    }
    fun getUserExist(): LiveData<Boolean>{
        return already
    }

//    suspend fun fetchChats(id: String) {
//        withContext(Dispatchers.IO) {
//            db.collection("Chats")
//                .addSnapshotListener { value, error ->
//                    error?.let {
//                        return@addSnapshotListener
//                    }
//                    value?.let {
//                        val listChat = ArrayList<ChatListModel>()
//                        if (!it.isEmpty) {
//                            for (doc in it) {
//                                if (doc.get("idOne").toString() == id && doc.get("idTwo")
//                                        .toString() != id
//                                ) {
//                                    val person = doc.toObject<ChatListModel>()
//                                    listChat.add(person);
//                                    if (person.chatId.toString().isNotEmpty()) {
//                                        db.collection("Chats")
//                                            .document(person.chatId.toString())
//                                            .collection("Message")
//                                            .get()
//                                            .addOnFailureListener {
//                                                listChat.remove(person)
//                                            }
//                                    } else {
//                                        Log.d("loi null:>>", "fetchChats")
//                                    }
//                                }
//                                if (doc.get("idTwo").toString() == id && doc.get("idOne")
//                                        .toString() != id
//                                ) {
//                                    val person = doc.toObject<ChatListModel>()
//                                    listChat.add(person)
//                                    if (person.chatId.toString().isNotEmpty()) {
//                                        db.collection("Chats")
//                                            .document(person.chatId.toString())
//                                            .collection("Message")
//                                            .get()
//                                            .addOnFailureListener {
//                                                listChat.remove(person)
//                                            }
//                                    } else {
//                                        Log.d("loi null:>>", "fetchChats")
//                                    }
//                                }
//                            }
//                            listChat.sortWith<ChatListModel>(object : Comparator<ChatListModel> {
//                                override fun compare(
//                                    time1: ChatListModel?,
//                                    time2: ChatListModel?
//                                ): Int {
//                                    if (time1 != null) {
//                                        if (time2 != null) {
//                                            if (time1.timeOne != null && time2.timeOne != null)
//                                                return time2.timeOne!!.compareTo(time1.timeOne)
//                                        }
//                                    }
//                                    return 0
//                                }
//                            })
//                            chatsLiveData.postValue(listChat)
//                        } else {
//                            chatsLiveData.postValue(ArrayList<ChatListModel>())
//                        }
//                    }
//                }
//        }
//    }


    suspend fun fetchChats(id: String) {
        withContext(Dispatchers.IO) {
            db.collection("Chats")
                .addSnapshotListener { value, error ->
                    error?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        val listChat = ArrayList<ChatListModel>()
                        if (!it.isEmpty) {
                            for (doc in it) {
                                if (doc.get("idOne").toString() == id && doc.get("idTwo")
                                        .toString() != id
                                ) {
                                    val person = doc.toObject<ChatListModel>()
                                    if(person.lastMessOne.toString().isNotEmpty()){
                                        listChat.add(person);
                                    }
                                }
                                if (doc.get("idTwo").toString() == id && doc.get("idOne")
                                        .toString() != id
                                ){
                                    val person = doc.toObject<ChatListModel>()
                                    if(person.lastMessOne.toString().isNotEmpty()){
                                        listChat.add(person);
                                    }
                                }
                            }
                            listChat.sortWith<ChatListModel>(object : Comparator<ChatListModel> {
                                override fun compare(
                                    time1: ChatListModel?,
                                    time2: ChatListModel?
                                ): Int {
                                    if (time1 != null) {
                                        if (time2 != null) {
                                            if (time1.timeOne != null && time2.timeOne != null)
                                                return time2.timeOne!!.compareTo(time1.timeOne)
                                        }
                                    }
                                    return 0
                                }
                            })
                            chatsLiveData.postValue(listChat)
                        } else {
                            chatsLiveData.postValue(ArrayList<ChatListModel>())
                        }
                    }
                }
        }
    }

    //kiem tra dang nhap
    fun checkUser(Email: String, passWord: String) {
        db.collection("Account")
            .get()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    val list = ArrayList<Account>()
                    for (document in it.result) {
                        val person = document.toObject<Account>()
                        if(person.Email==Email&&person.PassWord==passWord) {
                            list.add(person)
                        }
                    }
                    isAccount.value = list
                }
            })
            .addOnFailureListener(OnFailureListener {
                Log.d("dang nhap that bai", "that bai")
            })
    }


    //kiem tra dang ky
    fun checkUserSignUp(Email: String) {
        db.collection("Account")
            .get()
            .addOnCompleteListener{
                Log.d("my is mail o repository",Email.toString())
                var idAlready=false
                for(doc in it.result){
                    val account=doc.toObject<Account>()
                    if(account.Email==Email){
                        idAlready=true
                    }
                }
                already.value=idAlready
            }
            .addOnFailureListener {
                 already.value=false
            }
    }

    //dang ky
    fun addAccount(Email: String, passWord: String, Name: String) {
        val account = hashMapOf(
            "Email" to Email,
            "Name" to Name,
            "PassWord" to passWord,
            "uid" to "",
            "fcmToken" to ""
        )

        db.collection("Account")
            .add(account)
            .addOnCompleteListener(OnCompleteListener {
                val userModel = UserModel(Email, Name, "", "", "")
                makeUser(userModel, it.result.id);
            })
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    //create cuoc hoi thoai
    fun createCom(chatListModel: ChatListModel) {
        val communicate = hashMapOf(
            "chatId" to chatListModel.chatId,
            "idOne" to chatListModel.idOne,
            "nameOne" to chatListModel.nameOne,
            "imgOne" to chatListModel.imgOne,
            "idTwo" to chatListModel.idTwo,
            "nameTwo" to chatListModel.nameTwo,
            "imgTwo" to chatListModel.imgTwo,
            "lastMessOne" to chatListModel.lastMessOne,
        )
        db.collection("Chats")
            .add(communicate)
            .addOnCompleteListener {
                db.collection("Chats")
                    .document(it.result.id)
                    .update("chatId", it.result.id)
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding communicate document", it)
            }
    }

    //gui tin nhan
//    suspend fun sendMessage(messageModel: MessageModel, context: Context) {
//        withContext(Dispatchers.IO) {
//            val mess = hashMapOf(
//                "idChat" to messageModel.idChat,
//                "message" to messageModel.message,
//                "image" to messageModel.image,
//                "senderId" to messageModel.senderId,
//                "receiveId" to messageModel.receiveId,
//                "senderName" to messageModel.senderName,
//                "receiveImage" to messageModel.receiveImage,
//                "time" to FieldValue.serverTimestamp()
//            )
//            val list = listOf<String>(messageModel.senderId, messageModel.receiveId)
//            db.collection("Message")
//                .whereIn("idOne", list)
//                .whereIn("idTwo", list)
//                .get().addOnCompleteListener(OnCompleteListener {
//                    val querySnapshot = it.result
//                    for (document in querySnapshot) {
//                        if (document.get("idOne")
//                                .toString() == messageModel.senderId && document.get("idTwo")
//                                .toString() == messageModel.receiveId
//                        ) {
//                            db.collection("Chats")
//                                .document(document.id.toString())
//                                .collection("Message")
//                                .add(mess)
//                                .addOnSuccessListener {
//                                    //cap nhat thoiw gian gui
//                                    db.collection("Chats")
//                                        .document(document.id.toString())
//                                        .update("timeOne", FieldValue.serverTimestamp())
//                                    db.collection("Chats")
//                                        .document(document.id.toString())
//                                        .update(
//                                            "lastMessOne",
//                                            messageModel.message + " " + messageModel.senderId
//                                        )                                    //gui thong bao
//                                    db.collection("Account")
//                                        .whereEqualTo("uid", mess["receiveId"].toString())
//                                        .get()
//                                        .addOnCompleteListener(OnCompleteListener { re ->
//                                            for (doc in re.result) {
//                                                val person = doc.toObject<Account>()
//                                                Log.d("vxcvxcvxcv", person.toString())
//                                                sendNotificationToUser(
//                                                    AppOwner(context), person.fcmToken,
//                                                    mess["message"].toString()
//                                                )
//                                                Log.d(
//                                                    "token phai nhan :" + person.fcmToken,
//                                                    doc.toObject<Account>().fcmToken
//                                                )
//                                            }
//                                        })
//                                }
//                        }
//                        if (document.get("idTwo")
//                                .toString() == messageModel.senderId && document.get("idOne")
//                                .toString() == messageModel.receiveId
//                        ) {
//                            db.collection("Chats")
//                                .document(document.id.toString())
//                                .collection("Message")
//                                .add(mess)
//                                .addOnSuccessListener {
//                                    db.collection("Chats")
//                                        .document(document.id.toString())
//                                        .update("timeOne", FieldValue.serverTimestamp())
//                                    db.collection("Chats")
//                                        .document(document.id.toString())
//                                        .update(
//                                            "lastMessOne",
//                                            messageModel.message + " " + messageModel.senderId
//                                        )                                    //gui thong bao
//                                    db.collection("Account")
//                                        .whereEqualTo("uid", mess["receiveId"].toString())
//                                        .get()
//                                        .addOnCompleteListener(OnCompleteListener { re ->
//                                            for (doc in re.result) {
//                                                val person = doc.toObject<Account>()
//                                                sendNotificationToUser(
//                                                    AppOwner(context), person.fcmToken,
//                                                    mess["message"].toString()
//                                                )
//                                                Log.d(
//                                                    "token phai nhan :" + person.fcmToken,
//                                                    doc.toObject<Account>().fcmToken
//                                                )
//                                            }
//                                        })
//                                }
//                        }
//                    }
//                }).addOnFailureListener {
//                    Log.w("loi gui tin nhan", "Error send message to document", it)
//                }
//        }
//    }
    suspend fun sendMessage(messageModel: MessageModel, context: Context) {
        withContext(Dispatchers.IO) {
            val mess = hashMapOf(
                "idChat" to messageModel.idChat,
                "message" to messageModel.message,
                "senderId" to messageModel.senderId,
                "receiveId" to messageModel.receiveId,
                "senderName" to messageModel.senderName,
                "receiveImage" to messageModel.receiveImage,
                "time" to FieldValue.serverTimestamp()
            )
            db.collection("Message")
                .add(mess)
                .addOnCompleteListener {
                    db.collection("Chats")
                        .document(messageModel.idChat)
                        .update("timeOne", FieldValue.serverTimestamp());
                    db.collection("Chats")
                        .document(messageModel.idChat)
                        .update(
                            "lastMessOne",
                            messageModel.message + " " + messageModel.senderId)
                    db.collection("Account")
                        .whereEqualTo("uid",messageModel.receiveId)
                        .get()
                        .addOnCompleteListener(OnCompleteListener { re ->
                            for (doc in re.result) {
                                val person = doc.toObject<Account>();
                                sendNotificationToUser(
                                    AppOwner(context), person.fcmToken,
                                    mess["message"].toString()
                                );
                                Log.d("token phai nhan :" + person.fcmToken,
                                    doc.toObject<Account>().fcmToken)
                            } })
                }
        }
    }

    fun <T> update(key: String, value: T, App: AppOwner) {
        db.collection("Account")
            .whereEqualTo("uid", App.getUid())
            .get()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        db.collection("Account")
                            .document(doc.id)
                            .update(key, value)
                            .addOnCompleteListener(OnCompleteListener { })
                            .addOnFailureListener(OnFailureListener { })
                    }
                }
            })
    }

    //get message from firebase ban dau  vi khong co id
//    suspend fun fetchMessById(documentId: String) {
//        messageLiveData.value = ArrayList<MessageModel>()
//        withContext(Dispatchers.IO) {
//            db.collection("Chats")
//                .document(documentId)
//                .collection("Message")
//                .orderBy("time")
//                .addSnapshotListener { value, error ->
//                    error?.let {
//                        return@addSnapshotListener
//                    }
//                    value?.let {
//                        if (!it.isEmpty) {
//                            val lstMessage = ArrayList<MessageModel>()
//                            for (doc in it) {
//                                val mess = doc.toObject<MessageModel>()
//                                lstMessage.add(mess)
//                            }
//                            messageLiveData.value = lstMessage
//                        }
//                    }
//                }
//        }
//    }
      suspend fun fetchMessById(Id: String) {
        withContext(Dispatchers.IO) {
         db.collection("Message")
             .orderBy("time",Direction.ASCENDING)
             .addSnapshotListener{value,error->
                 error?.let {
                     return@addSnapshotListener
                 }
                 value?.let {
                     val lstMessage = ArrayList<MessageModel>()
                     if(!it.isEmpty){
                         for (doc in it){
                             val chat=doc.toObject<MessageModel>();
                             if(chat.idChat==Id) {
                               lstMessage.add(doc.toObject<MessageModel>())
                             }
                         }
                     }
                     messageLiveData.postValue(lstMessage)
                 }
             }

        }
    }

    fun getProfile(Id: String) {
        db.collection("Users")
            .document(Id)
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    val person = it.toObject<UserModel>()
                    profile.value = person
                    Log.d("owner : ", person?.image.toString())
                }
            }
    }

    //luu tru tai khoan
    fun saveAccount(activity: Activity, account: Account) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("Uid", account.uid)
            putString("Email", account.Email)
            apply()
        }
    }

    //create user
    private fun makeUser(userModel: UserModel, id: String) {
        val user = hashMapOf(
            "Email" to userModel.Email,
            "Name" to userModel.Name,
            "status" to userModel.status,
            "image" to userModel.image,
            "online" to userModel.online
        )
        db.collection("Users")
            .add(user)
            .addOnCompleteListener(OnCompleteListener {
                db.collection("Account")
                    .document(id)
                    .update("uid", it.result.id)
                    .addOnCompleteListener(OnCompleteListener { It ->
                        if (It.isSuccessful){
                            signUpSuccess.value=true
                        }
                    })
                    .addOnFailureListener(OnFailureListener {
                        signUpSuccess.value=false
                    })
                Log.d("them user thanh cong", it.result.id)
            })
    }

    //find user
    fun findUser(mark: String) {
        db.collection("Users")
            .whereEqualTo("Name", mark)
            .orderBy("Name")
            .startAt(mark)
            .endAt(mark + "\uf8ff")
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    if (!it.isEmpty) {
                        val list = ArrayList<UserModel>();
                        for (doc in it) {
                            list.add(doc.toObject())
                        }
                        userLiveData.value = list
                    } else {
                        userLiveData.value = ArrayList<UserModel>()
                    }
                }
            }
    }

    fun clearInvited() {
        isInvited.value = ArrayList<Invited>()
    }

    //lay nguoi dung dang online
    fun getFriendOnline(id: String) {
        db.collection("Users")
            .document(id)
            .collection("Contacts")
            .addSnapshotListener { value, error ->
                val lst = ArrayList<String>()
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    if (!it.isEmpty) {
                        for (doc in it) {
                            lst.add(doc.get("Email").toString())
                        }
                        db.collection("Users")
                            .whereIn("Email", lst)
                            .whereEqualTo("online", "true")
                            .addSnapshotListener { value, error ->
                                val lst1 = ArrayList<UserModel>()
                                error?.let { it1 ->
                                    return@addSnapshotListener
                                }
                                value?.let { it1 ->
                                    for (doc in it1) {
                                        val person = doc.toObject<UserModel>()
                                        lst1.add(person)
                                    }
                                    userLiveData.value = lst1
                                    Log.d("thay doi trang thai online ne", lst1.toString())
                                }
                            }
                    } else {
                        userLiveData.value = ArrayList<UserModel>()
                    }
                }

            }
    }

    //kiem tra loi moi ket ban,dung dieu kien hoac
    fun checkInvited(idOne: String, idTwo: String) {
        val listId = listOf<String>(idOne, idTwo)
        db.collection("invited")
            .whereIn("FromId", listId)
            .whereIn("ReceiveId", listId)
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    val list = ArrayList<Invited>()
                    for (doc in it) {
                        if (doc.get("FromId").toString() == idTwo) {
                            val invite = doc.toObject<Invited>()
                            list.add(invite)
                        } else if (doc.get("ReceiveId").toString() == idTwo) {
                            val invite = doc.toObject<Invited>()
                            list.add(invite)
                        }
                    }
                    isInvited.value = list
                }
            }
    }

    //kiem tra ban be
    fun checkFriend(idYourSelf: String, userModel: UserModel) {
        db.collection("Users")
            .document(idYourSelf)
            .collection("Contacts")
            .whereEqualTo("Email", userModel.Email)
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    var friend = ""
                    for (doc in it) {
                        friend = doc.get("Email").toString()
                    }
                    isFriend.value = friend != ""
                }
            }
    }

    fun getFriend(id: String) {
        db.collection("Users")
            .document(id)
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    var friend = UserModel()
                    friend = it.toObject<UserModel>()!!
                    frProfile.value = friend
                }
            }
    }

    //lay id tu email
    fun getId(Email: String) {
        db.collection("Users")
            .whereEqualTo("Email", Email)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                for (doc in it.result) {
                    idUser.value = doc.id.toString()
                    Log.d("id cua ban la:>>", idUser.value.toString())
                }
            })
            .addOnFailureListener(OnFailureListener {
                idUser.value = ""
            })
    }

    //huy ket ban
    fun cancelFriend(idYourSelf: String, idFriend: String, myEmail: String, userModel: UserModel) {
        db.collection("Users")
            .document(idYourSelf)
            .collection("Contacts")
            .whereEqualTo("Email", userModel.Email)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                var itId = ""
                for (doc in it.result) {
                    itId = doc.id.toString()
                    break
                }
                db.collection("Users")
                    .document(idYourSelf)
                    .collection("Contacts")
                    .document(itId)
                    .delete();
            })
            .addOnFailureListener(OnFailureListener { });
        db.collection("Users")
            .document(idFriend)
            .collection("Contacts")
            .whereEqualTo("Email", myEmail)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                var itId = ""
                for (doc in it.result) {
                    itId = doc.id.toString()
                }
                db.collection("Users")
                    .document(idFriend)
                    .collection("Contacts")
                    .document(itId)
                    .delete();
            })
            .addOnFailureListener(OnFailureListener { })
    }

    //dong y ket ban ban be
    fun addFriend(idYourSelf: String, idTwo: String, myEmail: String, userModel: UserModel) {
        val friend = hashMapOf(
            "Email" to userModel.Email.toString()
        )
        val my = hashMapOf(
            "Email" to myEmail
        )
        //themban  1
        db.collection("Users")
            .document(idYourSelf)
            .collection("Contacts")
            .add(friend)
        //them ban 2
        db.collection("Users")
            .document(idTwo)
            .collection("Contacts")
            .add(my)
        deleteRequest(idYourSelf, idTwo)

    }

    fun acceptFriend(idYourSelf: String, myEmail: String, Email: String) {
        val friend = hashMapOf(
            "Email" to Email
        )
        val my = hashMapOf(
            "Email" to myEmail
        )
        db.collection("Users")
            .whereEqualTo("Email", Email)
            .get()
            .addOnCompleteListener {
                for (doc in it.result) {
                    db.collection("Users")
                        .document(doc.id)
                        .collection("Contacts")
                        .add(my);
                    db.collection("Users")
                        .document(idYourSelf)
                        .collection("Contacts")
                        .add(friend)
                    deleteRequest(idYourSelf, doc.id)
                }
            }
    }

    //xoa loi moi ket ban
    fun deleteRequest(idOne: String, idTwo: String) {
        val list = listOf(idOne, idTwo)
        db.collection("invited")
            .whereIn("FromId", list)
            .whereIn("ReceiveId", list)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                var invited = ""
                for (doc in it.result) {
                    val item = doc.toObject<Invited>()
                    if (item.FromId == idOne && item.ReceiveId == idTwo) {
                        invited = doc.id
                        break
                    }
                    if (item.ReceiveId == idOne && item.FromId == idTwo) {
                        invited = doc.id
                        break
                    }
                }
                if (invited.isNotEmpty()) {
                    db.collection("invited")
                        .document(invited)
                        .delete()
                }
            })
    }

    fun deleteRequest1(idOne: String, Email: String) {
        db.collection("Users")
            .whereEqualTo("Email", Email)
            .get()
            .addOnCompleteListener {
                it.result.forEach { n1 ->
                    val list = listOf(idOne, n1.id);
                    db.collection("invited")
                        .whereIn("FromId", list)
                        .whereIn("ReceiveId", list)
                        .get()
                        .addOnCompleteListener(OnCompleteListener {
                            var invited = "";
                            for (doc in it.result) {
                                val item = doc.toObject<Invited>();
                                if (item.FromId == idOne && item.ReceiveId == n1.id) {
                                    invited = doc.id;
                                    break
                                };
                                if (item.ReceiveId == idOne && item.FromId == n1.id) {
                                    invited = doc.id;
                                    break
                                }
                            };
                            if (invited.isNotEmpty())
                                db.collection("invited")
                                    .document(invited)
                                    .delete()
                        })
                }
            }
    }

    //yeu cau ket ban
    fun requestFriend(idOne: String, idTwo: String) {
        val request = hashMapOf(
            "FromId" to idOne,
            "ReceiveId" to idTwo,
            "time" to FieldValue.serverTimestamp()
        )
        db.collection("invited")
            .add(request)
    }

    fun offLive(id: String) {
        db.collection("Users")
            .document(id)
            .update("online", "false")
    }

    fun onLive(id: String) {
        db.collection("Users")
            .document(id)
            .update("online", "true")
    }

    fun setImage(imageUri: ByteArray, id: String, time: String) {
        val storage = storageRef.child("images/" + time + ".jpg")
        db.collection("Users")
            .document(id)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful && it.result.get("image").toString().isNotEmpty()) {
                    val photoRef: StorageReference =
                        Firebase.storage.getReferenceFromUrl(it.result.get("image").toString())
                    photoRef.delete().addOnSuccessListener {

                    }.addOnFailureListener(OnFailureListener {

                    })
                }
            })
        storage.putBytes(imageUri)
            .addOnSuccessListener(OnSuccessListener {
                storage.downloadUrl.addOnSuccessListener { uri ->
                    db.collection("Users")
                        .document(id)
                        .update("image", uri)
                    db.collection("Chats")
                        .get()
                        .addOnCompleteListener(OnCompleteListener {
                            for (doc in it.result) {
                                val idOne = doc.get("idOne").toString()
                                val idTwo=doc.get("idTwo").toString()
                                if (idOne == id)
                                    db.collection("Chats")
                                        .document(doc.id)
                                        .update("imgOne", uri)
                                else if (idTwo == id)
                                    db.collection("Chats")
                                        .document(doc.id)
                                        .update("imgTwo", uri)
                            }
                        })
                }
            })
    }

    fun sendNotificationToUser(App: AppOwner, toUser: String, message: String) {
        App.getUid()?.let {
            db.collection("Users")
                .document(it)
                .get()
                .addOnCompleteListener(OnCompleteListener { It ->
                    val currentUser = It.result.toObject<UserModel>()
                    Log.d(
                        "lay id de push notification thanh cong",
                        "thanh cong thanh cong thanh cong>>>>>"
                    )
                    try {
                        val jsonObject = JSONObject()

                        val notificationObj = JSONObject()
                        notificationObj.put("title", currentUser!!.Name)
                        notificationObj.put("body", message)

                        val dataObj = JSONObject()
                        dataObj.put("userId", App.getUid())

                        jsonObject.put("notification", notificationObj)
                        jsonObject.put("data", dataObj)
                        jsonObject.put("to", toUser)

                        callApi(jsonObject)
                    } catch (e: Exception) {

                    }
                })
        }

    }

    private fun callApi(jsonObject: JSONObject) {
        val json: MediaType = "application/json; charset=utf-8".toMediaType()
        val client = OkHttpClient()
        val url = "https://fcm.googleapis.com/fcm/send"
        val body: RequestBody = jsonObject.toString().toRequestBody(json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .header(
                "Authorization",
                "Bearer AAAA-CrwQ4Q:APA91bFkwK73GIDiOLSpg4n7pdh7Pu3R4dw4kxHjOiEhkuvG8Xpo9rkqgTEZGQ9crjEMALr8I_BCx55CVcE0WI1xSnJ-xNvK68NKdD_ZbqB4cTZy-gDoDM1ejrBW4j9i-t6TNCrZfJB3"
            )
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(
                        "that bai that bai that bai that bai that bai that bai",
                        "that bai that bai that bai that bai that bai that bai"
                    )
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d(
                        "thanh cong thanh cong thanh cong thanh cong",
                        "thanh cong thanh cong thanh cong thanh cong"
                    )
                }

            })
    }

    //    fun getRequest(id: String, context: Context) {
//        db.collection("invited")
//            .whereEqualTo("ReceiveId", id)
//            .orderBy("time")
//            .addSnapshotListener{ value, error ->
//                var lstUser = ArrayList<UserModel>();
//                error?.let{
//                  return@addSnapshotListener
//                }
//                value?.let {
//                    val list = ArrayList<String>();
//                    for (doc in it.documents) {
//                        list.add(doc.get("FromId").toString().trim());
//                    }
//                         Log.d("sisez list:.....:", list.toString())
//                        db.collection("Users")
//                            .addSnapshotListener { value, error ->
//                                error?.let {
//                                    return@addSnapshotListener
//                                }
//                                value?.let { It ->
//                                    It.forEach { n ->
//                                        Log.d("lan luot:....:", n.id)
//                                        if (list.contains(n.id.toString())) {
//                                            val user = n.toObject<UserModel>()
//                                            lstUser.add(user)
//                                            Log.d("lan:....:", n.id)
//                                        }
//                                    }
//                                    request.value = lstUser
//                                }
//                            }
//                }
//            }
//    }
    fun getRequest(id: String, context: Context) {
        db.collection("invited")
            .whereEqualTo("ReceiveId", id)
            .orderBy("time", Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val list = ArrayList<String>();
                for (doc in it) {
                    list.add(doc.get("FromId").toString().trim());
                };
                val lstUser = ArrayList<UserModel>(Collections.nCopies(list.size, UserModel()));
                Log.d("giam dan: ", list.toString())
                db.collection("Users")
                    .get()
                    .addOnCompleteListener { n ->
                        if (n.isSuccessful && !n.result.isEmpty)
                            for (doc in n.result)
                                if (list.contains(doc.id)) {
                                    lstUser[list.indexOf(doc.id.toString())] =
                                        doc.toObject<UserModel>()
                                }
                        request.value = lstUser
                    }
            }
            .addOnFailureListener {
                Log.d("that bai", "that bbai")
            }
    }

    fun findChatExist(idOne: String, idTwo: String) {
        val list = listOf<String>(idOne, idTwo)
        db.collection("Chats")
            .whereIn("idOne", list)
            .addSnapshotListener { value, error ->
                error?.let {
                    return@addSnapshotListener
                }
                value?.let {
                    if (!it.isEmpty){
                        for (doc in it){
                            Log.d("lay duoc chat ne:", doc.get("idOne").toString())
                            if (doc.get("idOne").toString() == idOne && doc.get("idTwo")
                                    .toString() == idTwo
                            ){
                                val chat = doc.toObject<ChatListModel>();
                                chatExist.value = chat
                                Log.d(
                                    "lay duoc id roi ne",
                                    doc.toObject<ChatListModel>().toString()
                                )
                            }else if (doc.get("idOne").toString() == idTwo && doc.get("idTwo")
                                    .toString() == idOne
                            ) {
                                val chat = doc.toObject<ChatListModel>();
                                chatExist.value = chat
                                Log.d(
                                    "lay duoc id roi ne",
                                    doc.toObject<ChatListModel>().toString()
                                )
                            }
                        }
                    }
                }
            }
    }
}


