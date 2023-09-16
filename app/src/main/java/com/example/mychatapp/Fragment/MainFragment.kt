package com.example.mychatapp.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mychatapp.Adapter.MainViewAdapter
import com.example.mychatapp.R
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.MainfragmentBinding

class MainFragment(context:Context,fa: AppCompatActivity):Fragment() {
    private lateinit var binding: MainfragmentBinding
    private val mContext=context
    private val mainViewModel= MainViewModel.getInstance()
    private val mFa=fa
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=MainfragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.containMain.let {
            it.adapter= MainViewAdapter(mFa,mContext)
        }
        binding.containMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                when(position){
                   0->binding.BtbChat.selectedItemId=R.id.ChatMenuId
                   1->binding.BtbChat.selectedItemId=R.id.ContactMenuId
                   2->binding.BtbChat.selectedItemId=R.id.SettingMenuId
                   else->binding.containMain.currentItem=R.id.ChatMenuId
                }
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        binding.BtbChat.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ChatMenuId-> {
                    binding.containMain.currentItem = 0
                    true
                }
                R.id.ContactMenuId->{
                    binding.containMain.currentItem=1
                    true
                }
                R.id.SettingMenuId->{
                    binding.containMain.currentItem=2
                    true
                }
                else->{
                    binding.containMain.currentItem=0
                    true
                }
            }
        }
    }
}