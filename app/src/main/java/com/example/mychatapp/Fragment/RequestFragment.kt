package com.example.mychatapp.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatapp.Adapter.RequestAdapter
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.RequestFragmentBinding

class RequestFragment(context:Context):Fragment() {
    private lateinit var binding:RequestFragmentBinding
    private val mContext=context
    private val mainViewModel=MainViewModel.getInstance()
    private  var lstRequest=ArrayList<UserModel>()
    private lateinit var fragmentActivity:FragmentActivity
    private lateinit var menuHost: MenuHost
    private val sharedPref = mContext.getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE)
    private  val result= sharedPref.getString("Uid","")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= RequestFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentActivity = requireActivity();
        mainViewModel.getRequest(AppOwner(mContext).getUid().toString(),mContext)
        val requestAdapter=RequestAdapter(mContext)
        requestAdapter.onClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                if (result != null) {
                    mainViewModel.makeCheckFriend(result,lstRequest[position])
                    mainViewModel.makeUid(lstRequest[position].Email.toString())
                    passData(lstRequest[position],ProfileFragment(mContext))
                }
            }
        })
        binding.rcvRequest.let {
            it.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
            it.adapter=requestAdapter
        }
        mainViewModel.getRequest().observe(viewLifecycleOwner, Observer {
            lstRequest=it
            requestAdapter.submitList(lstRequest)
        })

        val appBar=(requireActivity() as AppCompatActivity)
        appBar.setSupportActionBar(binding.requestToolB)
        appBar.supportActionBar.let {
            it?.setDisplayHomeAsUpEnabled(true)
        }
        menuHost= requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
               menuInflater.inflate(R.menu.rqmenu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home->{
                        if(this@RequestFragment.isAdded)
                           this@RequestFragment.fragmentActivity.supportFragmentManager.popBackStack()
                        else{
                          Log.d("loi gan fragment","loi gan fragment")
                        }
                    }
                }
               return true
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity =  context as FragmentActivity;
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

    override fun onResume() {
        super.onResume();
    }




}