package vn.hexagon.vietnhat.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.base.ui.BaseDiffUtil
import vn.hexagon.vietnhat.data.model.remote.ChatContent
import vn.hexagon.vietnhat.data.model.remote.ChatDetail
import vn.hexagon.vietnhat.databinding.ItemMessageReceiveBinding
import vn.hexagon.vietnhat.databinding.ItemMessageSendBinding

/**
 * Created by NhamVD on 2019-09-22.
 */
class ChatDetailAdapter(private val userId: String, private val userAvatar: String) :
    BaseAdapter<ChatContent>(BaseDiffUtil()) {
    private val VIEW_TYPE_SEND = 1
    private val VIEW_TYPE_RECEIVE = 2

    private var chatDetail: ChatDetail? = null
    private var chatContent: List<ChatContent> = ArrayList()

    fun setData(chatDetail: ChatDetail) {
        this.chatDetail = chatDetail
        chatContent = chatDetail.content
        submitList(chatContent)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (userId) {
            chatContent[position].userIdSend -> VIEW_TYPE_SEND
            else -> VIEW_TYPE_RECEIVE
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return when (viewType) {
            VIEW_TYPE_SEND -> DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_message_send,
                parent,
                false
            )
            else -> DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_message_receive,
                parent,
                false
            )
        }
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val item = getItem(position)
        when (binding) {
            is ItemMessageSendBinding -> {
                binding.data = item
                if (position == chatContent.size - 1) {
                    Glide.with(binding.itemMessageReceiveAvatarImage).load(userAvatar)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.ic_ava_nodata)
                        .error(R.drawable.ic_ava_nodata)
                        .into(binding.itemMessageReceiveAvatarImage)
                    binding.itemMessageReceiveAvatarImage.visibility = VISIBLE
                } else {
                    binding.itemMessageReceiveAvatarImage.visibility = GONE
                }
            }
            is ItemMessageReceiveBinding -> {
                binding.data = item
                Glide.with(binding.itemMessageReceiveAvatarImage).load(chatDetail?.userAvatar2)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_ava_nodata)
                    .error(R.drawable.ic_ava_nodata)
                    .into(binding.itemMessageReceiveAvatarImage)
            }
        }
    }
}