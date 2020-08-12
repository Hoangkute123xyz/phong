package vn.hexagon.vietnhat.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.data.model.comment.Comment
import vn.hexagon.vietnhat.data.model.comment.GiftCommentItem
import vn.hexagon.vietnhat.databinding.ItemGiftCommentBinding
import vn.hexagon.vietnhat.databinding.LayoutCommentRowBinding
import java.util.*
import kotlin.collections.ArrayList

class CommentPhoneGiftAdapter(private val phoneGiftViewModel: PhoneGiftViewModel) :
    BaseAdapter<GiftCommentItem>(ListDiffCallback()) {

    //List comment
    private var listComment = ArrayList<GiftCommentItem>()

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
       return DataBindingUtil.inflate(
           LayoutInflater.from(parent.context),
           R.layout.item_gift_comment,
           parent,
           false)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        /*when (binding) {
            is ItemGiftCommentBinding -> {
                binding.position = position
                binding.viewModel = phoneGiftViewModel
                binding.imageUserAvatar.clipToOutline = true
            }
        }*/
    }

    companion object {
        class ListDiffCallback : DiffUtil.ItemCallback<GiftCommentItem>() {
            override fun areItemsTheSame(oldItem: GiftCommentItem, newItem: GiftCommentItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GiftCommentItem, newItem: GiftCommentItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}