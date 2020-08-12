package vn.hexagon.vietnhat.ui.chat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.fragment_unlogin_common.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.view.DividerItemDecorator
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.chat.GroupChat
import vn.hexagon.vietnhat.data.model.remote.ChatList
import vn.hexagon.vietnhat.databinding.FragmentChatListBinding
import vn.hexagon.vietnhat.ui.chat.adapter.ChatListAdapter
import javax.inject.Inject

/**
 * Created by NhamVD on 2019-09-22.
 */
class ChatListFragment : MVVMBaseFragment<FragmentChatListBinding, ChatListViewModel>(),
    View.OnClickListener, ChatListAdapter.OnChatItemClick {
    private lateinit var chatListViewModel: ChatListViewModel
    private var chatAdapter: ChatListAdapter? = null

    @Inject
    lateinit var preferences: SharedPreferences
    private val userId: String? by lazy {
        preferences.getString(getString(R.string.variable_local_user_id), Constant.BLANK)
    }

    private fun isToken(): Boolean {
        val token = preferences.getString(
            getString(R.string.token),
            Constant.BLANK
        )
        return token == Constant.TOKEN
    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        simpleTitleText = getString(R.string.home_bottom_menu_messenger)
        leftButtonVisible = false
        rightButtonVisible = true
        rightButtonResource = R.drawable.ic_group_add

        rightActionBarButton.setOnClickListener {
            var action = ChatListFragmentDirections.actionChatListFragmentToAddGroupFragment("","")
            findNavController().navigate(action)
        }
    }

    override fun isShowBottomNavigation(): Boolean = true

    override fun isActionBarOverlap(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_chat_list

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun getBaseViewModel(): ChatListViewModel {
        chatListViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ChatListViewModel::class.java)
        return chatListViewModel
    }

    override fun initView() {
        chatListRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val divider = activity?.let { context ->
                ContextCompat.getDrawable(context, R.drawable.divider)?.let { drawable ->
                    DividerItemDecorator(
                        drawable
                    )
                }
            }
            divider?.let { addItemDecoration(divider) }
            itemAnimator = DefaultItemAnimator()
            chatAdapter = ChatListAdapter()
            chatAdapter!!.onItemClickListener = this@ChatListFragment
            adapter = chatAdapter
        }



        tvLastContent.setOnClickListener(this)
        tvSummary.setOnClickListener(this)
        imageCommunity.setOnClickListener(this)

        Glide.with(context!!).load(R.drawable.ic_item_social)
            .apply(RequestOptions.circleCropTransform())
            .into(imageCommunity)

        chatListViewModel.getCommunityContent(userId!!)
        chatListViewModel.communityData.observe(this, Observer { response ->
            run {
                response.data?.let { communityContentList ->
                    tvLastContent.text =
                        communityContentList[communityContentList.size - 1].content
                }
            }
        })

        chatListViewModel.getGroupChatList(userId!!)

        chatListViewModel.groupListResponse.observe(viewLifecycleOwner, Observer {
            chatAdapter?.submitList(it.data)
        })


    }

    override fun initData(argument: Bundle?) {

    }

    override fun initAction() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.unLoginRegister -> findNavController().navigate(R.id.registerFragment)
            R.id.unLoginSignIn -> findNavController().navigate(R.id.loginFragment)
            R.id.tvLastContent,
            R.id.imageCommunity,
            R.id.tvSummary -> {
                var action  = ChatListFragmentDirections.actionChatListFragmentToCommunityDetailFragment("")
                findNavController().navigate(action)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        if (isToken()) {
            favUnLoginArea.visibility = View.GONE
            chatListViewModel.getChatList(userId!!)
        } else {
            favUnLoginArea.visibility = View.VISIBLE
            unLoginRegister.setOnClickListener(this)
            unLoginSignIn.setOnClickListener(this)
        }
    }

    private fun clickItem(item: ChatList) {
        item.userId?.let {
            val action = ChatListFragmentDirections
                .actionChatListFragmentToChatDetailFragment(
                    item.userId,
                    item.userName,
                    item.userAvatar,
                    item.userPhone
                )
            findNavController().navigate(action)
        }
    }

    override fun onClick(item: GroupChat) {
//        item.userId?.let {
//            val action = ChatListFragmentDirections
//                .actionChatListFragmentToChatDetailFragment(
//                    item.userId,
//                    item.userName,
//                    item.userAvatar,
//                    item.userPhone
//                )
//            findNavController().navigate(action)
//        }

        var action  = ChatListFragmentDirections.actionChatListFragmentToCommunityDetailFragment(item.id)
        findNavController().navigate(action)
    }
}