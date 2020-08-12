package vn.hexagon.vietnhat.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.base.ui.BaseDiffUtil
import vn.hexagon.vietnhat.data.model.chat.GroupChat
import vn.hexagon.vietnhat.data.model.remote.ChatList
import vn.hexagon.vietnhat.databinding.ItemChatListBinding
import vn.hexagon.vietnhat.databinding.ItemGroupChatListBinding

/**
 * Created by NhamVD on 2019-09-22.
 */
class ChatListAdapter() :
    BaseAdapter<GroupChat>(BaseDiffUtil()) {

    var onItemClickListener: OnChatItemClick? = null

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemGroupChatListBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_group_chat_list,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val bindingItem = binding as? ItemGroupChatListBinding
        val item = getItem(position)
        bindingItem?.let {
            with(it) {
                tvSummary.text = item.name
                Glide.with(imageGroup).load(item.avatar).apply(RequestOptions.circleCropTransform())
                    .into(imageGroup)
                item.lastMessage?.let { lastMessage -> tvLastContent.text = lastMessage }
            }
        }
        bindingItem?.root?.setOnClickListener {
            onItemClickListener?.onClick(item)
        }
    }

    public interface OnChatItemClick {
        fun onClick(item: GroupChat)
    }
}