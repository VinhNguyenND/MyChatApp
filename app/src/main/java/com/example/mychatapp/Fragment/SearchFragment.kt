package com.example.mychatapp.Fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView.OnCloseListener
import android.widget.Toast
import com.example.mychatapp.Interface.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.Adapter.UserAdapter
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.databinding.SearchFragmentBinding

class SearchFragment(context:Context):Fragment() {
    private val mContext=context
    private lateinit var  binding:SearchFragmentBinding
    private val mainViewModel=MainViewModel.getInstance()
    private val userAdapter=UserAdapter(mContext)
    private var listUser=ArrayList<UserModel>()
    private  val result= AppOwner(mContext).getUid()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=SearchFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appBar=(requireActivity() as AppCompatActivity)
        appBar.setSupportActionBar(binding.SearchToolBar)
        appBar.supportActionBar.let {
            it?.setDisplayHomeAsUpEnabled(true)
            it?.setDisplayShowHomeEnabled(true)
        }

        userAdapter.onClickListener(object :OnItemClickListener{
            override fun onItemClick(position: Int) {
                if (result != null) {
                    mainViewModel.makeCheckFriend(result,listUser[position])
                    mainViewModel.makeUid(listUser[position].Email.toString())
                }
                passData(listUser[position],ProfileFragment(mContext))
            }
        })
        binding.rcvMessage.let {
            it.adapter=userAdapter
            it.layoutManager=LinearLayoutManager(mContext,RecyclerView.VERTICAL,false)
        }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
               menuInflater.inflate(R.menu.ffmenu, menu)
               val searchManager=mContext.getSystemService(Context.SEARCH_SERVICE) as SearchManager
               val searchView =(menu.findItem(R.id.searchFf).actionView as SearchView)
               searchView.apply {
                   setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
               }
                searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                       TODO()
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null)
                            mainViewModel.findUser(newText)
                            .observe(viewLifecycleOwner, Observer {
                                listUser = it
                                userAdapter.subMissList(it)
                            });
                        if(newText=="") userAdapter.subMissList(ArrayList<UserModel>());
                        return true
                    }
                });
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home->
                    {
                        val fragmentManager: FragmentManager =parentFragmentManager
                        val transaction=fragmentManager.beginTransaction()
                        transaction.setTransition(TRANSIT_FRAGMENT_CLOSE)
                        fragmentManager.popBackStack()
                        transaction.commit()
                    }
                }
               return true
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

     private fun replaceFragment(fragment: Fragment){
        val fragmentManager: FragmentManager =parentFragmentManager
        val fragmentTransition: FragmentTransaction =fragmentManager.beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN)
        fragmentTransition.replace(R.id.MainFrame,fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }

    fun passData(userModel: UserModel,fragment: Fragment){
        val transaction = parentFragmentManager.beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_OPEN)
        val bundle=Bundle()
        bundle.putParcelable("userProfile",userModel)
        fragment.arguments=bundle
        transaction.replace(R.id.MainFrame,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}


