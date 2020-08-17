package vn.hexagon.vietnhat.ui.chat.community

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_community_detail.*
import kotlinx.android.synthetic.main.fragment_community_detail.favUnLoginArea
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_unlogin_common.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.databinding.FragmentCommunityDetailBinding
import vn.hexagon.vietnhat.ui.home.HomeFragmentDirections
import java.util.*
import javax.inject.Inject

class CommunityDetailFragment :
    MVVMBaseFragment<FragmentCommunityDetailBinding, CommunityDetailViewModel>() {


    private var actionBar: SimpleActionBar? = null

    @Inject
    lateinit var preferences: SharedPreferences

    private val userId: String? by lazy {
        preferences.getString(
            getString(vn.hexagon.vietnhat.R.string.variable_local_user_id),
            Constant.BLANK
        )
    }
    private val userImageUrl: String? by lazy {
        preferences.getString(
            getString(R.string.variable_local_avt),
            Constant.BLANK
        )
    }
    private val userName: String? by lazy {
        preferences.getString(
            getString(R.string.variable_local_name),
            Constant.BLANK
        )
    }

    private var images: ArrayList<Image>? = arrayListOf()

    private lateinit var communityDetailViewModel: CommunityDetailViewModel

    var groupID: String? = null

    private var chatAdapter: CommunityChatAdapter? = null

    private var isCommunityChat = true


    override fun getBaseViewModel(): CommunityDetailViewModel {
        communityDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CommunityDetailViewModel::class.java)
        return communityDetailViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun initData(argument: Bundle?) {
        communityDetailViewModel.communityContentResponse.observe(
            viewLifecycleOwner,
            Observer { pair ->
                pair.first?.let {
                    chatAdapter?.setData(pair.second)
                }
            })


    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        leftButtonVisible = true
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftActionBarButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_community_detail

    override fun initView() {

        // Init layout by condition
        if (!isToken()) {
            processBeforeLogin()
        } else {
            processAfterLogin()
        }
    }

    fun onClickImg(list: ArrayList<String>, position: Int) {
        val listImg = arrayOfNulls<String>(list.size)
        list.toArray(listImg)

        val action =
            CommunityDetailFragmentDirections.actionCommunityDetailFragmentToZoomFragment(
                listImg,
                position
            )
        findNavController().navigate(action)
    }

    private fun processAfterLogin() {
        favUnLoginArea.visibility = View.GONE
        inputMessageChatDetailLayout.visibility=View.VISIBLE
        sendMessageChatDetailButton.visibility=View.VISIBLE
        imageChooseMedia.visibility=View.VISIBLE

        communityDetailViewModel.userAvatar = userImageUrl
        communityDetailViewModel.userName = userName

        imageChooseMedia.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setRootDirectoryName("VietNhat")
                .setDirectoryName("Image")
                .setMultipleMode(false)
                .setSelectedImages(images)
                .setRequestCode(100)
                .start()
        }

        actionBar = baseActionBar as SimpleActionBar?

        try {
            groupID = arguments?.let {
                CommunityDetailFragmentArgs.fromBundle(it).groupID
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            groupID = ""
        }

        if (!groupID.isNullOrBlank()) {
            //Is group chat
            isCommunityChat = false
            actionBar?.apply {
                simpleTitleText = "Tin nhắn nhóm"
                rightButtonVisible = true
                rightButtonResource = R.drawable.ic_group_add

                userId?.let { communityDetailViewModel.getGroupChatList(it) }
            }
            communityDetailViewModel.getGroupContent(userId!!, groupID!!)
            communityDetailViewModel.subIncomingMessageGroup(userId!!, groupID!!)

        } else {
            //Is community chat
            actionBar?.simpleTitleText = "Cộng đồng"
            communityDetailViewModel.getContent(userId!!)
            communityDetailViewModel.subIncomingMessageComm(userId!!)
        }

        communityDetailViewModel.groupListResponse.observe(this, Observer {
            actionBar?. rightActionBarButton?.setOnClickListener {view->
                var json = Gson()
                var userList =""
                for (item in it.data){
                    if (!TextUtils.isEmpty(groupID) && item.id.equals(groupID)){
                        userList = json.toJson(item.listUserId)
                    }
                }
                var action =
                    CommunityDetailFragmentDirections.actionCommunityDetailFragmentToAddGroupFragment(
                        groupID!!,userList
                    )
                findNavController().navigate(action)
            }
        })

        chatDetailRecycler.apply {
            recycledViewPool.setMaxRecycledViews(1, 0)
            recycledViewPool.setMaxRecycledViews(2, 0)
            setHasFixedSize(true)
            val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
            manager.stackFromEnd = true
            layoutManager = manager
            itemAnimator = DefaultItemAnimator()
            chatAdapter = CommunityChatAdapter(userId!!, userImageUrl!!, ::onClickImg)
            adapter = chatAdapter

            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
//                if (bottom < oldBottom) {
//                    adapter?.itemCount?.let { pos -> smoothScrollToPosition(pos) }
//                }
                try {
                    smoothScrollToPosition(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun processBeforeLogin() {
        favUnLoginArea.visibility = View.VISIBLE
        unLoginRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        unLoginSignIn.setOnClickListener{
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun initAction() {
        inputMessageChatDetailEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (isCommunityChat) {
                    communityDetailViewModel.sendMessage(
                        userId!!,
                        "1",
                        inputMessageChatDetailEdit.text.toString(),
                        null
                    )
                } else {
                    communityDetailViewModel.sendMessage(
                        userId!!,
                        groupID!!,
                        inputMessageChatDetailEdit.text.toString(),
                        "1",//Type = 1 -> send text
                        null
                    )
                }
                inputMessageChatDetailEdit.setText("")
                true
            } else {
                false
            }
        }

        sendMessageChatDetailButton.setOnClickListener {
            if (inputMessageChatDetailEdit.text.trim().isNotEmpty()) {
                if (isCommunityChat) {
                    communityDetailViewModel.sendMessage(
                        userId!!,
                        "1",
                        inputMessageChatDetailEdit.text.toString(),
                        null
                    )
                } else {
                    communityDetailViewModel.sendMessage(
                        userId!!,
                        groupID!!,
                        inputMessageChatDetailEdit.text.toString(),
                        "1",//Type = 1 -> send text
                        null
                    )
                }
                inputMessageChatDetailEdit.setText("")
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)
            // Do stuff with image's path or id. For example:
            images?.let {
                if (isCommunityChat) {
                    communityDetailViewModel.sendMessage(
                        userId!!,
                        "2",
                        inputMessageChatDetailEdit.text.toString(),
                        images
                    )
                } else {
                    communityDetailViewModel.sendMessage(
                        userId!!,
                        groupID!!,
                        inputMessageChatDetailEdit.text.toString(),
                        "2",//Type = 2 -> send image
                        images
                    )
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Get token
     *
     * @return token (true is hasToken)
     */
    private fun isToken(): Boolean {
        val token = preferences.getString(
            getString(R.string.token),
            Constant.BLANK
        )
        return token == Constant.TOKEN
    }
}