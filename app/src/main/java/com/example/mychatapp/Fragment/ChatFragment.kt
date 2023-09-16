package com.example.mychatapp.Fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mychatapp.Adapter.MessageAdapter
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.ChatListModel
import com.example.mychatapp.Model.MessageModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.ViewModel.MessageViewModel
import com.example.mychatapp.databinding.MessagefragmentBinding
import kotlinx.coroutines.*
import kotlin.math.log

class ChatFragment(context: Context):Fragment() {
    private lateinit var  binding:MessagefragmentBinding
    private  var listChat=ArrayList<MessageModel>()
    private lateinit var messageAdapter: MessageAdapter
    private val mContext=context
    private val mainViewModel=MainViewModel.getInstance()
    private var messageViewModel=MessageViewModel()
    private  val result= AppOwner(context).getUid()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=MessagefragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        binding.btnBackMess.setOnClickListener{
            replaceFragment(MainFragment(mContext,requireActivity() as AppCompatActivity))
        }
        messageAdapter= MessageAdapter(mContext)
        messageAdapter.onClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {

            }
        });
        messageViewModel.getMess().observe(viewLifecycleOwner, Observer {
            messageAdapter.submitList(it)
        })
        if(arguments!=null) {
            binding.rcvMessage.apply {
                val linear= LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                linear.stackFromEnd=true
                this.layoutManager=linear
                adapter=messageAdapter
            };
        }
        binding.btnNhan.setOnClickListener(View.OnClickListener {
                if (binding.txtNhap.text.toString() != "") {
                    messageViewModel.sendMess(getSender(), mContext)
                    binding.txtNhap.text = null
                    Log.d("sender:>>>",getSender().toString())
                }

        })

    }

    override fun onResume() {
        super.onResume()
        val a:String?=requireArguments().getString("chatId")
        if (a != null) {
            messageViewModel.getMessById(a)
        }else{
            Log.d("requireArguments().getString(chatId)"," nulll")
        }
    }

    private fun changeData():ChatListModel{
        return ChatListModel(
            requireArguments().getString("chatId"),
            requireArguments().getString("idOne"),
            requireArguments().getString("nameOne"),
            requireArguments().getString("imageOne"),
            requireArguments().getString("idTwo"),
            requireArguments().getString("nameTwo"),
            requireArguments().getString("imageTwo"),
            ""
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager =parentFragmentManager
        val fragmentTransition: FragmentTransaction =fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.MainFrame,fragment)
        fragmentTransition.commit()
    }
    private fun getSender():MessageModel {
            val id:String?= requireArguments().getString("chatId");
            val idOne: String? =requireArguments().getString("idOne");
            val idTwo: String? =requireArguments().getString("idTwo");
            val nameOne: String? =requireArguments().getString("nameOne");
            val nameTwo: String? =requireArguments().getString("NameTwo");
            val imgOne: String? =requireArguments().getString("imgOne");
            val imgTwo:String?=requireArguments().getString("imgTwo");
            var senderId= ""
            var receiveId = ""
            var senderName = ""
            var image=" "
            Log.d("day nay:>>","id:$id idOne:$idOne idTwo:$idTwo nameOne:$nameOne nameTwo:$nameTwo")
            if (idOne != null && idTwo != null) {
                if (idOne == result) {
                    senderId = idOne
                    receiveId = idTwo
                    if (nameOne != null) {
                        senderName = nameOne
                    }
                    if(imgOne!=null){
                        image=imgOne
                    }
                } else {
                    senderId = idTwo
                    receiveId = idOne
                    if (nameTwo != null) {
                        senderName = nameTwo
                    }
                    if(imgTwo!=null){
                        image=imgTwo
                    }
                }
            }
             Log.d("day be:>>",MessageModel("",senderId,receiveId,"",senderName,binding.txtNhap.text.toString()).toString())
            return MessageModel(id.toString(),senderId,receiveId,image,senderName,binding.txtNhap.text.toString())
    }
    private fun bindData(){
        if(result!=requireArguments().getString("idOne")){
            binding.NameFr.text=requireArguments().getString("nameOne");
            Glide.with(context!!)
                .load(requireArguments().getString("imageOne"))
                .error(R.drawable.blank_avatar)
                .fitCenter()
                .into(binding.imgAvatar)
        }
        if(result!=requireArguments().getString("idTwo")){
            binding.NameFr.text=requireArguments().getString("nameTwo");
            Glide.with(context!!)
                .load(requireArguments().getString("imageTwo"))
                .error(R.drawable.blank_avatar)
                .fitCenter()
                .into(binding.imgAvatar)
        }
    }
}