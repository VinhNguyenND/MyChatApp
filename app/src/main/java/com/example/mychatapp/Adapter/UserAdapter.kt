package com.example.mychatapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.databinding.UserLayoutBinding

class UserAdapter(context:Context):RecyclerView.Adapter<UserAdapter.UserHolder>() {
     private lateinit var mOnClick:OnItemClickListener
     private val mContext=context
     private var list:ArrayList<UserModel> = ArrayList<UserModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun subMissList(list:ArrayList<UserModel>){
        this.list=list
        notifyDataSetChanged()
    }
    fun onClickListener(onClick: OnItemClickListener){
       this.mOnClick=onClick
    }
    class UserHolder(private val binding: UserLayoutBinding,onClick: OnItemClickListener,private val context: Context):RecyclerView.ViewHolder(binding.root){
      init {
            binding.apply {
                this.root.setOnClickListener(View.OnClickListener {
                    onClick.onItemClick(adapterPosition)
                })
            }
        }
        fun bindData(userModel: UserModel){
         Glide.with(context)
                 .load(userModel.image)
                 .error(R.drawable.blank_avatar)
                 .fitCenter()
                 .into(binding.imageUser)
            binding.NameUser.text=userModel.Name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding =UserLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserHolder(binding,mOnClick,mContext)
    }

    override fun getItemCount(): Int {
        if(list.isNotEmpty()){
            return list.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindData(list[position])
    }
}