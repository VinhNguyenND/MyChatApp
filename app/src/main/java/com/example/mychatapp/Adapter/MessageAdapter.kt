package com.example.mychatapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.MessageModel
import com.example.mychatapp.R
import com.example.mychatapp.databinding.MessagefragmentBinding
import com.example.mychatapp.databinding.SendLeftBinding
import com.example.mychatapp.databinding.SendRightBinding

//khai bao nham listadapter co the dan den bao loi

class MessageAdapter(context:Context): ListAdapter<MessageModel,MessageAdapter.MessViewHolder>(messDiffUtil()) {
   private lateinit var mOnClick: OnItemClickListener
   private val mRight=0
   private val mLeft=1
   private val mContext=context
   private val sharedPref = mContext.getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE)
   private  val result= sharedPref.getString("Uid","")

   fun onClickListener(onClick: OnItemClickListener){
       this.mOnClick=onClick
   }
    class MessViewHolder( val binding: ViewBinding, onClick: OnItemClickListener):RecyclerView.ViewHolder(binding.root){
      init {
          binding.apply {
              root.setOnClickListener{
                  onClick.onItemClick(adapterPosition)
              }
          }
      }
    }
    class messDiffUtil: DiffUtil.ItemCallback<MessageModel>(){
        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return  newItem.message==oldItem.message
        }
        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return  areItemsTheSame(oldItem,newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessViewHolder {
        return if (viewType==mRight){
            val binding = SendRightBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            MessViewHolder(binding,this.mOnClick)
        } else{
            val binding = SendLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            MessViewHolder(binding,this.mOnClick)
        }
    }

    override fun onBindViewHolder(holder: MessViewHolder, position: Int) {
      val messageModel:MessageModel=getItem(position)
      if(getItem(position).senderId==result){
          (holder.binding as SendRightBinding).apply {
           holder.binding.rcMessage.text=messageModel.message.toString()
          }
      }
        else{
            (holder.binding as SendLeftBinding).apply {
                holder.binding.leftMess.text=messageModel.message.toString()
          }
      }
    }


    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).senderId==result){
            mRight
        } else {
            mLeft
        }
    }
}