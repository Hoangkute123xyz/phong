package vn.hexagon.vietnhat.ui.favourite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.data.model.comment.GiftFavouriteItem
import vn.hexagon.vietnhat.data.model.comment.ItemFavoritePhone
import vn.hexagon.vietnhat.databinding.ItemGiftFavouriteBinding
import vn.hexagon.vietnhat.ui.comment.PhoneGiftViewModel

class GiftFavouriteAdapter() :
    BaseAdapter<ItemFavoritePhone>(ListDiffCallback()) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gift_favourite,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        when (binding) {
            is ItemGiftFavouriteBinding -> {
                Glide.with(binding.likedMemberAvt)
                    .load(currentList[position].avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.likedMemberAvt)
                binding.likedMemberName.text = currentList[position].name
            }
        }
    }

    companion object {
        class ListDiffCallback : DiffUtil.ItemCallback<ItemFavoritePhone>() {
            override fun areItemsTheSame(
                oldItem: ItemFavoritePhone,
                newItem: ItemFavoritePhone
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ItemFavoritePhone,
                newItem: ItemFavoritePhone
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}