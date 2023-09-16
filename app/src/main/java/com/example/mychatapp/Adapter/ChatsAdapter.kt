package com.example.mychatapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.ChatListModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.databinding.ItemchatsBinding

class ChatsAdapter(context:Context): ListAdapter<ChatListModel, ChatsAdapter.userViewHolder>(UserDiffUtil()) {

    private lateinit var mOnClick:OnItemClickListener
    private  val result= AppOwner(context).getUid()
    private val mContext=context
   fun onClickListener(onClick: OnItemClickListener){
       this.mOnClick=onClick
   }

    class userViewHolder(private val binding:ItemchatsBinding,onClick:OnItemClickListener,private val context: Context):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                this.root.setOnClickListener(View.OnClickListener {
                    onClick.onItemClick(adapterPosition)
                })
            }
        }
        fun bindData(userModel: ChatListModel,result: String) {
            val message=userModel.lastMessOne.toString();
            val last=message.lastIndexOf(" ")
            var mess :String=""
            var mess1:String=""
            if(last>0) {
                mess = message.substring(0,last+1 )
                mess1 = message.substring(last+1,message.length)
            }
            if (result == userModel.idOne) {
                if(mess1==userModel.idOne) {
                    binding.title.text = "My:"
                }
                if(mess.length<30) {
                    binding.lastMess.text = mess
                }else{
                    val mess2= mess.substring(0,30)+"..."
                    binding.lastMess.text=mess2
                }
                binding.NameMess.text = userModel.nameTwo
                Glide.with(context)
                 .load(userModel.imgTwo)
                 .error(R.drawable.blank_avatar)
                 .fitCenter()
                 .into(binding.imageAvatar)
            }else if(result==userModel.idTwo){
                if(mess1==userModel.idTwo) {
                    binding.title.text = "Your:"
                };
                if(mess.length<30) {
                    binding.lastMess.text = mess
                }else{
                    val mess2=mess.substring(0,29)+"...."
                    binding.lastMess.text=mess2
                }
                binding.NameMess.text = userModel.nameOne
               Glide.with(context)
                 .load(userModel.imgOne)
                 .error(R.drawable.blank_avatar)
                 .fitCenter()
                 .into(binding.imageAvatar)
            }
        }
    }


    class UserDiffUtil:DiffUtil.ItemCallback<ChatListModel>(){
        override fun areItemsTheSame(oldItem: ChatListModel, newItem: ChatListModel): Boolean {
            return newItem.lastMessOne==oldItem.lastMessOne
        }
        override fun areContentsTheSame(oldItem: ChatListModel, newItem: ChatListModel): Boolean {
            return  areItemsTheSame(oldItem,newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int,): userViewHolder {
        val binding =ItemchatsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return userViewHolder(binding,this.mOnClick,mContext)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        if (result != null) {
            holder.bindData(getItem(position),result)
        }
    }


}