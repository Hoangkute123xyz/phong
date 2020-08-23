package vn.hexagon.vietnhat.ui.list.translator

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.post.Post
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.databinding.LayoutItemListServiceTranslatorBinding
import vn.hexagon.vietnhat.databinding.NetworkStateItemBinding
import vn.hexagon.vietnhat.ui.list.PostListViewModel
import vn.hexagon.vietnhat.ui.list.WindyConvertUtil

/**
 *
//                       _ooOoo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                       O\ = /O
//                   ____/`---'\____
//                 .   ' \\| |// `.
//                  / \\||| : |||// \
//                / _||||| -:- |||||- \
//                  | | \\\ - /// | |
//                | \_| ''\---/'' | |
//                 \ .-\__ `-` ___/-. /
//              ______`. .' /--.--\ `. . __
//           ."" '< `.___\_<|>_/___.' >'"".
//          | | : `- \`.;`\ _ /`;.`/ - ` : | |
//            \ \ `-. \_ __\ /__ _/ .-` / /
//    ======`-.____`-.___\_____/___.-`____.-'======
//                       `=---='
//
//    .............................................
//                    Pray for no Bugs
 * =====================================================
 * Name：VuNBT
 * Create on：2019-09-25
 * =====================================================
 */
class TranslatorListAdapter(private val viewModel: PostListViewModel,
                            private val onClick: (String, String) -> Unit,
                            private val onClickFav: (Boolean) -> Unit): BaseAdapter<Post>(ListDiffCallback()) {

    // Network state
    private var networkState: NetworkState? = null

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return if (viewType == Constant.LIST_ITEM_TYPE) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_list_service_translator,
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
            is LayoutItemListServiceTranslatorBinding -> {
                binding.position = position
                binding.viewmodel = viewModel
                var isAdded = false
                binding.previewImg.clipToOutline = true
                binding.itemPriceText.getPhoneNumber(position)
                /*if (viewModel.userId != Constant.BLANK) {
                    binding.iconFavourite.visibility = View.VISIBLE
                    binding.itemPriceText.priceStateProcess(position, true)
                } else {
                    binding.itemPriceText.priceStateProcess(position, false)
                    binding.iconFavourite.visibility = View.GONE
                }*/
                binding.iconFavourite.setOnClickListener {
                    if (currentList[position].isFavourite == 0 && !isAdded) {
                        isAdded = true
                        binding.previewImg.clipToOutline = true
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
                }
                // Handle onClick item
                binding.translatorListItemArea.setOnClickListener {
                    onClick(getItem(position).userId, getItem(position).id)
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

    /**
     * Bind phone number
     *
     * @param position
     */
    private fun TextView.getPhoneNumber(position: Int) {
        this.text = context.getString(R.string.contact_lbl, currentList[position].phone)
    }

    /**
     * Handle display price when login or not
     *
     * @param position
     * @param isLogin
     */
    private fun TextView.priceStateProcess(position: Int, isLogin: Boolean) {
        val format = when(currentList[position].translateType) {
            "1" -> R.string.dynamic_money_per_hour
            "2" -> R.string.dynamic_money_per_month
            else -> R.string.dynamic_money_per_hour
        }
        if (isLogin) {
            this.apply {
                text = resources.getString(format,
                    String.format("%,d", WindyConvertUtil.filterNumeric(currentList[position].price).toInt()))
                setTextColor(context.getColor(R.color.color_pink_d14b79))
                typeface = Typeface.DEFAULT_BOLD
            }
        } else {
            this.apply {
                text = context.getString(R.string.price_not_login)
                setTextColor(context.getColor(R.color.color_text_link))
                typeface = Typeface.DEFAULT
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

    companion object {
        class ListDiffCallback : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}