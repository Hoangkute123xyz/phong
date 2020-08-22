package vn.hexagon.vietnhat.ui.list.phone

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.Fone
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.databinding.ItemFoneHouseListBinding
import vn.hexagon.vietnhat.databinding.NetworkStateItemBinding
import vn.hexagon.vietnhat.ui.list.PostListViewModel

class FoneHouseListAdapter(
    private val viewModel: PostListViewModel,
    private val onClick: (String, String) -> Unit
) : BaseAdapter<Fone>(ListDiffCallback()) {

    // Network state
    private var networkState: NetworkState? = null

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return if (viewType == Constant.LIST_ITEM_TYPE) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_fone_house_list,
                parent,
                false
            )
        } else {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.network_state_item,
                parent,
                false
            )
        }
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        when (binding) {
            is ItemFoneHouseListBinding -> {
                binding.position = position
                binding.viewmodel = viewModel
                var isAdded = false
                binding.imageView.clipToOutline = true
                // Control display iconFav
                /*if (viewModel.userId != Constant.BLANK) {
                    binding.iconFavourite.visibility = View.VISIBLE
                } else {
                    binding.iconFavourite.visibility = View.GONE
                }
                binding.iconFavourite.setOnClickListener {
                    if (currentList[position].isFavourite == 0 && !isAdded) {
                        isAdded = true
                        binding.iconFavourite.setImageResource(R.drawable.ic_save_active)
                        viewModel.addFavouriteRequest(viewModel.userId, currentList[position].id)
                        currentList[position].isFavourite = 1
                    } else {
                        isAdded = false
                        binding.iconFavourite.setImageResource(R.drawable.ic_save)
                        viewModel.removeFavouriteRequest(viewModel.userId, currentList[position].id)
                        currentList[position].isFavourite = 0
                    }
                    onClickFav(isAdded)
                }*/

                binding.itemTitle.text = getItem(position).name.trim() + " " + getItem(position).memory
                binding.itemMemory.text = getItem(position).memory //hiden
                binding.itemStatus.text = getItem(position).status
                binding.itemLastText.text = getItem(position).price
                binding.itemOldPrice.text = getItem(position).priceDiscount

                // Handle onClick item
                binding.phoneListItemArea.setOnClickListener {
                    onClick(viewModel.userId, getItem(position).id)
                }
                if (!getItem(position).img.isNullOrEmpty() && !TextUtils.isEmpty(getItem(position).img[0])) {
                    Glide.with(binding.imageView).load(getItem(position).img[0])
                        .into(binding.imageView)
                }
                binding.itemBH.text = String.format("%s %s", "Bảo hành", getItem(position).warrant)
                if (!TextUtils.isEmpty(getItem(position).priceDiscount)) {
                    binding.lineOldValue.visibility = View.VISIBLE
                } else {
                    binding.lineOldValue.visibility = View.GONE
                }
            }
            is NetworkStateItemBinding -> {
                binding.viewmodel = viewModel
                binding.isShowProgress =
                    networkState != null && networkState == NetworkState.LOADING
                if (networkState != null && networkState == NetworkState.ERROR) {
                    binding.isShowErrMessage = true
                    binding.message = networkState!!.msg
                } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                    binding.isShowErrMessage = true
                    binding.message = networkState!!.msg
                } else {
                    binding.isShowErrMessage = false
                }
            }
        }
    }

    companion object {
        class ListDiffCallback : DiffUtil.ItemCallback<Fone>() {
            override fun areItemsTheSame(oldItem: Fone, newItem: Fone): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Fone, newItem: Fone): Boolean {
                return oldItem == newItem
            }

        }
    }

    /**
     * Return network state status if have more row
     *
     * @return
     */
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    /**
     * Get item count
     *
     * @return items count
     */
    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    /**
     * Get view type
     *
     * @param position
     * @return network/item type
     */
    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            Constant.NETWORK_ITEM_TYPE
        } else {
            Constant.LIST_ITEM_TYPE
        }
    }

    /**
     * Handle network state with whole screen progressBar
     *
     * @param newNetworkState
     */
    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                                          // If hadExtraRow = true & hasExtraRow = false
                notifyItemRemoved(super.getItemCount())                 // Remove last item(progressBar)
            } else {                                                    // hadExtraRow = false & hasExtraRow = true
                notifyItemInserted(super.getItemCount())                // add ProgressBar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) {   // If hasExtraRow = true & hadExtraRow = true
            notifyItemChanged(itemCount - 1)
        }
    }
}