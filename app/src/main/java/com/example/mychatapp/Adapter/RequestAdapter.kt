package com.example.mychatapp.Adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.ItemRequestBinding

class RequestAdapter(context:Context):ListAdapter<UserModel,RequestAdapter.viewHolder>(RequestDiffUtil()) {
    private lateinit var mOnClick: OnItemClickListener
    private val mContext=context
    private val  mainViewModel=MainViewModel.getInstance()
    fun onClickListener(onClick: OnItemClickListener){
       this.mOnClick=onClick
    }
    class viewHolder(val binding: ItemRequestBinding, onClick: OnItemClickListener,private val context: Context,private val mainViewModel: MainViewModel): RecyclerView.ViewHolder(binding.root){
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
                .into(binding.imgRequest)
            binding.acceptRequest.setOnClickListener {
                it.visibility=View.INVISIBLE
                binding.deleteRequest.visibility=View.INVISIBLE
                binding.invited.visibility=View.VISIBLE
                userModel.Email?.let { it1 ->
                    mainViewModel.acceptFriend(AppOwner(context).getUid().toString(),AppOwner(context).getMyEmail().toString(),
                        it1
                    )
                }
            }
            binding.deleteRequest.setOnClickListener {
                it.visibility=View.INVISIBLE
                binding.acceptRequest.visibility=View.INVISIBLE
                binding.deleted.visibility=View.VISIBLE
                userModel.Email?.let { it1 ->
                    mainViewModel.deleteRequest1(AppOwner(context).getUid().toString(),AppOwner(context).getMyEmail().toString(),
                    )
                }            }
            binding.nameRequest.text=userModel.Name.toString();
        }
    }

    class RequestDiffUtil: DiffUtil.ItemCallback<UserModel>(){
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return  oldItem.Name==newItem.Name
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return areItemsTheSame(oldItem,newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding=ItemRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding,mOnClick,mContext,mainViewModel)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val user:UserModel=getItem(position)
        holder.bindData(user)

    }

}