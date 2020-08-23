package vn.hexagon.vietnhat.ui.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.data.model.comment.Comment
import vn.hexagon.vietnhat.data.model.fone.FoneComment
import vn.hexagon.vietnhat.databinding.ItemGiftCommentBinding
import vn.hexagon.vietnhat.databinding.ItemPhonehouseCommentBinding
import vn.hexagon.vietnhat.databinding.LayoutCommentRowBinding

class CommentFoneHouseAdapter(private val viewModel:PhoneGiftViewModel,private val context: Context):BaseAdapter<FoneComment.DataField>(ListDiffCallback()){
    companion object {
        class ListDiffCallback : DiffUtil.ItemCallback<FoneComment.DataField>() {
            override fun areItemsTheSame(oldItem: FoneComment.DataField, newItem: FoneComment.DataField): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FoneComment.DataField, newItem: FoneComment.DataField): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_phonehouse_comment,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        when (binding) {
            is ItemPhonehouseCommentBinding -> {
                binding.position = position
                binding.viewModel = viewModel
                binding.imageUserAvatar.clipToOutline=true
            }
        }
    }

    override fun getItemCount(): Int {
        val value = viewModel.listFoneHouseCommentResponse.value
        return value?.data?.size ?: 0
    }
}