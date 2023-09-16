package com.example.mychatapp.Fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mychatapp.Interface.OnItemClickListener
import com.example.mychatapp.Model.ChatListModel
import com.example.mychatapp.Model.Invited
import com.example.mychatapp.Model.UserModel
import com.example.mychatapp.R
import com.example.mychatapp.Util.AppOwner
import com.example.mychatapp.ViewModel.MainViewModel
import com.example.mychatapp.ViewModel.MessageViewModel
import com.example.mychatapp.databinding.BottomSheetPfBinding
import com.example.mychatapp.databinding.ProfileFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileFragment(context: Context) : Fragment() {
    private lateinit var binding: ProfileFragmentBinding
    private val mContext = context
    private val mainViewModel = MainViewModel.getInstance()
    private val messageViewModel = MessageViewModel.getInstance()
    private var checkFriend: Boolean = false
    private var uid = ""
    private var ownerId = AppOwner(mContext).getUid().toString()
    private var ownerEmail = AppOwner(mContext).getMyEmail().toString()
    private var owner: UserModel = UserModel()
    private var chatList: ChatListModel? = null
    private var invited = ArrayList<Invited>()
    private var chatExist:ChatListModel?=null
    private lateinit var userProfile: UserModel
    private val myData = AppOwner(mContext)
    private val ketBan = "Kết Bạn"//an vao la gui loi moi
    private val xoaLm = "Đã gửi"//an vao la xoa loi moi
    private val TraLoi = "Trả Lời"//an vao chap nhan hoac xoa
    private val banBe = "Bạn Bè"//an vao la huy ket ba

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickAction()
        Log.d("my id:>>",ownerId)
        if (getIntentParcelable("userProfile") != null) {
            userProfile = getIntentParcelable("userProfile")!!;
            binding.btnSendMess.setOnClickListener {
                openChat();
            }
             binding.btnSAndA.setOnClickListener(View.OnClickListener {
                    action(userProfile)
                })
                Log.d("null", "getIntentParcelable !null")
        } else {
            userProfile = UserModel()
            Log.d("null", "getIntentParcelable null")
        }
        setProperty(userProfile);
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getIntentParcelable(name: String): UserModel? {
        return arguments?.getParcelable("userProfile", UserModel::class.java)
    }

    private fun openBottomSheet() {
        val addActionBottom = ActionBottomDialogFragment()
        addActionBottom.setListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    R.id.accept -> {
                        mainViewModel.acceptFriend(ownerId, uid, ownerEmail, userProfile);
                        chatList?.let {
                           mainViewModel.createCom(it)
                        }
                    }
                    R.id.deleteInvitation -> {
                        mainViewModel.deleteRequest(ownerId, uid)
                    }
                }
                binding.btnSAndA.text = "Đã phản hồi"
            }
        })
        addActionBottom.show(parentFragmentManager, "ActionBottomDialog")
    }

    private fun setProperty(userProfile: UserModel) {
        binding.nameProfile.text = userProfile.Name
        Glide.with(mContext)
            .load(userProfile.image)
            .error(R.drawable.blank_avatar)
            .fitCenter()
            .into(binding.profileAvatar)
    }

    private fun checkInvited(invited: ArrayList<Invited>) {
        if (invited.isNotEmpty()) {
            val invite = invited[0]
            if (invite.FromId == myData.getUid()) {
                binding.btnSAndA.text = xoaLm
            } else {
                binding.btnSAndA.text = TraLoi
            }
        }
    }

    private fun checkFriend(isFriend: Boolean) {
        if (isFriend) {
            binding.btnSAndA.text = banBe
        } else {
            binding.btnSAndA.text = ketBan
        }
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.clearInvited()
        binding.btnSAndA.text = ketBan
    }

    override fun onResume() {
        super.onResume()
        makeWithData();
        setChatList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun makeWithData() {
        mainViewModel.getUid().observe(viewLifecycleOwner, Observer {
            uid = it
            myData.getUid()?.let { it1 -> mainViewModel.makeCheckInvited(it1, uid) }
            mainViewModel.checkChatExist(uid,ownerId)
            Log.d("id nay profile:>>>",it.toString())
        })
        mainViewModel.getFriend().observe(viewLifecycleOwner, Observer {
            checkFriend = it
            checkFriend(checkFriend)
        })

        mainViewModel.getFriendProfile().observe(viewLifecycleOwner, Observer {
            Log.d("friend:>>>", it.toString())
        })

        mainViewModel.getInvited().observe(viewLifecycleOwner, Observer {
            invited = it
            if (invited.isNotEmpty()) {
                checkInvited(invited)
            }
        })

        mainViewModel.makeOwner(ownerId)

        mainViewModel.profileSetting().observe(viewLifecycleOwner, Observer {
            owner = it
            Log.d("profile cua my:>>",it.toString())
        })

        mainViewModel.getChatExist().observe(viewLifecycleOwner, Observer {
            chatExist=it
        })
    }

    private fun openChat() {
        Log.d("is friend", checkFriend.toString())
        if (checkFriend) {
            if(chatExist!=null) {
                val fragment = ChatFragment(mContext);
                passFragment(chatExist!!, fragment)
            }
        } else {
            Toast.makeText(mContext, "chi co ban be moi nhan tin cho nha duoc", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setChatList(){
        chatList= ChatListModel(
            "",
            uid,
            userProfile.Name.toString(),
            userProfile.image.toString(),
            ownerId,
            owner.Name.toString(),
            owner.image.toString(),
            "",)
    }

    private fun clickAction() =
        binding.backOfProfile.setOnClickListener(View.OnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().remove(this).setTransition(TRANSIT_FRAGMENT_CLOSE)
                .commit()
            fragmentManager.popBackStack()
        })

    private fun action(userProfile: UserModel) {
        when (binding.btnSAndA.text.toString()) {
            ketBan -> {
                if (AppOwner(mContext).getUid().toString() != uid) {
                    mainViewModel.requestFriend(AppOwner(mContext).getUid().toString(), uid)
                } else {
                    Toast.makeText(mContext, "không thể kết bạn với tài khoản", Toast.LENGTH_LONG)
                        .show()
                }
            }
            TraLoi -> {
                openBottomSheet()
            }
            xoaLm -> {
                mainViewModel.deleteRequest(AppOwner(mContext).getUid().toString(), uid)
            }
            banBe -> {
                mainViewModel.blockFriend(
                    AppOwner(mContext).getUid().toString(),
                    uid,
                    AppOwner(mContext).getMyEmail().toString(),
                    userProfile
                )
            }
        }
    }

    private fun passFragment(chatListModel: ChatListModel, fragment: Fragment) {
        val bundle = Bundle();
        bundle.putString("chatId", chatListModel.chatId);
        bundle.putString("idOne", chatListModel.idOne);
        bundle.putString("idTwo", chatListModel.idTwo);
        bundle.putString("nameOne", chatListModel.nameOne);
        bundle.putString("nameTwo", chatListModel.nameTwo)
        bundle.putString("imageOne", chatListModel.imgOne)
        bundle.putString("imageTwo", chatListModel.imgTwo)
        val transaction = parentFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(R.id.MainFrame, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }
}


class ActionBottomDialogFragment() : BottomSheetDialogFragment(), View.OnClickListener {
    private var mainViewModel = MainViewModel.getInstance()
    private lateinit var binding: BottomSheetPfBinding
    private lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPfBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accept.setOnClickListener(this)
        binding.deleteInvitation.setOnClickListener(this)
    }

    fun setListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onClick(v: View?) {
        if (v != null) {
            this.onItemClickListener.onItemClick(v.id)
            dismiss()
        }
    }

}
