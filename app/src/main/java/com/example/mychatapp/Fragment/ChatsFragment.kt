package com.example.mychatapp.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.Adapter.ChatsAdapter
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.ChatListModel
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.ViewModel.MessageViewModel
import com.example.mychatapp.databinding.ChatsfragmentBinding

class ChatsFragment(context: Context):Fragment(){

    private lateinit var binding:ChatsfragmentBinding
    private val mainViewModel=MainViewModel.getInstance()
    private val messageViewModel=MessageViewModel.getInstance()
    private  var listChats=ArrayList<ChatListModel>()
    private val mContext=context
    private lateinit var chatsAdapter:ChatsAdapter
    private val sharedPref = mContext.getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE)
    private  val result= sharedPref.getString("Uid","")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=ChatsfragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
        chatsAdapter= activity?.let { ChatsAdapter(it.applicationContext) }!!
        chatsAdapter.onClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                passData(listChats[position])
                messageViewModel.getMessById(listChats[position].chatId.toString())
            }
        })
        if (result != null) {
            mainViewModel.getChats(result).observe(viewLifecycleOwner, Observer {
                listChats=it
                chatsAdapter.submitList(listChats)
            })
        }
        binding.recyclerChats.apply {
            layoutManager= LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            adapter=chatsAdapter
        }
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager =parentFragmentManager
        val fragmentTransition: FragmentTransaction =fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.MainFrame,fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }
    fun passData(chatListModel:ChatListModel) {
        val bundle = Bundle();
        bundle.putString("chatId", chatListModel.chatId);
        bundle.putString("idOne", chatListModel.idOne);
        bundle.putString("idTwo", chatListModel.idTwo);
        bundle.putString("nameOne", chatListModel.nameOne);
        bundle.putString("nameTwo", chatListModel.nameTwo)
        bundle.putString("imageOne", chatListModel.imgOne)
        bundle.putString("imageTwo", chatListModel.imgTwo)
        val transaction = parentFragmentManager.beginTransaction();
        val fragmentTwo = ChatFragment(mContext);
        fragmentTwo.arguments = bundle;
        transaction.replace(R.id.MainFrame, fragmentTwo);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit()
   }

}