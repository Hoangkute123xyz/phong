package vn.hexagon.vietnhat.ui.chat.community

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
import vn.hexagon.vietnhat.data.model.chat.CommunityContent
import vn.hexagon.vietnhat.data.model.chat.CommunityContentResponse
import vn.hexagon.vietnhat.databinding.ItemCommunityReceiveBinding
import vn.hexagon.vietnhat.databinding.ItemCommunitySendBinding

class CommunityChatAdapter(
    private val userId: String,
    private val userAvatar: String,
    private val onClickImg: (ArrayList<String>, Int) -> Unit
) :
    BaseAdapter<CommunityContent>(BaseDiffUtil()) {

    private val VIEW_TYPE_SEND = 1
    private val VIEW_TYPE_RECEIVE = 2

    private var chatDetail: CommunityContentResponse? = null
    private var chatContent: List<CommunityContent> = ArrayList()
    private val imgList = ArrayList<String>()

    fun setData(chatDetail: CommunityContentResponse) {
        this.chatDetail = chatDetail
        chatContent = chatDetail.data
        submitList(chatContent)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (userId) {
            chatContent[position].userId -> VIEW_TYPE_SEND
            else -> VIEW_TYPE_RECEIVE
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return when (viewType) {
            VIEW_TYPE_SEND -> DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_community_send,
                parent,
                false
            )
            else -> DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_community_receive,
                parent,
                false
            )
        }
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        val item = getItem(position)
        when (binding) {
            is ItemCommunitySendBinding -> {
                binding.data = item
                if (position == 0) {
                    Glide.with(binding.itemMessageReceiveAvatarImage).load(userAvatar)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.ic_ava_nodata)
                        .error(R.drawable.ic_ava_nodata)
                        .into(binding.itemMessageReceiveAvatarImage)
                    binding.itemMessageReceiveAvatarImage.visibility = View.VISIBLE
                } else {
                    binding.itemMessageReceiveAvatarImage.visibility = View.GONE
                }

                when (item.type) {
                    "1" -> {
                        binding.imageContent.visibility = View.GONE
                        binding.itemMessageSendContentText.visibility = View.VISIBLE
                        binding.itemMessageSendContentText.setText(item.content)
                    }
                    "2" -> {
                        binding.itemMessageSendContentText.visibility = View.GONE
                        binding.imageContent.visibility = View.VISIBLE

                        Glide.with(binding.imageContent).load(item.content)
                            .placeholder(R.drawable.ic_chat_image)
                            .error(R.drawable.ic_chat_image)
                            .apply(RequestOptions.fitCenterTransform())
                            .into(binding.imageContent)
                        imgList.clear()
                        imgList.add(item.content)
                        binding.imageContent.setOnClickListener {
                            onClickImg(imgList, position)
                        }
                    }
                }

            }
            is ItemCommunityReceiveBinding -> {
                binding.data = item
                Glide.with(binding.itemMessageReceiveAvatarImage).load(item.userAvatar)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_ava_nodata)
                    .error(R.drawable.ic_ava_nodata)
                    .into(binding.itemMessageReceiveAvatarImage)
                binding.tvUserName.text = item.userName
                when (item.type) {
                    "1" -> {
                        binding.imageContent.visibility = View.GONE
                        binding.itemMessageReceiveContentText.visibility = View.VISIBLE
                        binding.itemMessageReceiveContentText.setText(item.content)
                    }
                    "2" -> {
                        binding.itemMessageReceiveContentText.visibility = View.GONE
                        binding.imageContent.visibility = View.VISIBLE
                        Glide.with(binding.imageContent).load(item.content)
                            .placeholder(R.drawable.ic_chat_image)
                            .error(R.drawable.ic_chat_image)
                            .apply(RequestOptions.fitCenterTransform())
                            .into(binding.imageContent)

                        imgList.clear()
                        imgList.add(item.content)
                        binding.imageContent.setOnClickListener {
                            onClickImg(imgList, position)
                        }
                    }
                }
            }

        }
    }
}