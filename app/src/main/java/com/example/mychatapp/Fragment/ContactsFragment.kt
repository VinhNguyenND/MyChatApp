package com.example.mychatapp.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.Adapter.UserAdapter
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.ContactsfragmentBinding

class ContactsFragment(context:Context):Fragment() {
    private val mainViewModel=MainViewModel.getInstance()
    private val mContext=context
    private val userAdapter= UserAdapter(mContext)
    private var listUser=ArrayList<UserModel>()
    private lateinit var binding:ContactsfragmentBinding
    private val sharedPref = mContext.getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE)
    private  val result= sharedPref.getString("Uid","")

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=ContactsfragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(this@ContactsFragment.isAdded) {
            binding.addFf.setOnClickListener {
                replaceFragment(SearchFragment(mContext))
            }
            binding.frRequest.setOnClickListener {
                replaceFragment(RequestFragment(mContext))
            }
        }
        userAdapter.onClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                if (result != null) {
                    mainViewModel.makeCheckFriend(result,listUser[position])
                }
                    mainViewModel.makeUid(listUser[position].Email.toString())
                    passData(listUser[position],ProfileFragment(mContext))
            }
        })
         if (result != null) {
            mainViewModel.makePerOnline(result)
            mainViewModel.getUser().observe(viewLifecycleOwner, Observer {
                listUser=it
                userAdapter.subMissList(listUser)
            })
        }
        binding.ctOnline.let {
            it.adapter=userAdapter
            it.layoutManager=LinearLayoutManager(mContext,RecyclerView.VERTICAL,false)
        }

    }
    fun passData(userModel: UserModel,fragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_OPEN);
        val bundle=Bundle();
        bundle.putParcelable("userProfile",userModel);
        fragment.arguments=bundle;
        transaction.replace(R.id.MainFrame,fragment);
        transaction.addToBackStack(null);
        transaction.commit()
    }
        private  fun replaceFragment(fragment: Fragment){
        val fragmentManager=parentFragmentManager
        val fragmentTransition: FragmentTransaction =fragmentManager.beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN)
        fragmentTransition.replace(R.id.MainFrame,fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }
}