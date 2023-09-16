package com.example.mychatapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mychatapp.Fragment.ChatsFragment
import com.example.mychatapp.Fragment.ContactsFragment
import com.example.mychatapp.Fragment.SettingFragment

class MainViewAdapter(fa:AppCompatActivity,context:Context): FragmentStateAdapter(fa) {
    private val mContext=context
    private var positionBegin:Int?=null
    private val itemCount=3
    override fun getItemCount(): Int {
        return itemCount
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
                0 -> ChatsFragment(mContext)
                1 -> ContactsFragment(mContext)
                2 -> SettingFragment(mContext)
                else ->ChatsFragment(mContext)
            }
    }
}