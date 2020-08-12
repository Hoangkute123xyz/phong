package vn.hexagon.vietnhat.ui.list.phone

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.ui.BaseAdapter
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.Brand
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.databinding.ItemFoneFilterBinding
import vn.hexagon.vietnhat.repository.list.PostListRepository


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
 * Create on：2019-09-26
 * =====================================================
 */
class FilterListAdapter : BaseAdapter<Brand>(ListDiffCallback()) {

    var repository: PostListRepository? = null

    var context: Context? = null

    var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    var layout: LinearLayout? = null


    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return if (viewType == Constant.LIST_ITEM_TYPE) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_fone_filter,
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
            is ItemFoneFilterBinding -> {
                val item = getItem(position)
                item?.let {
                    binding.tvBrand.setText(item.name)
                    Glide.with(binding.imageBrand).load(item.img)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.ic_phone_cell)
                        .error(R.drawable.ic_phone_cell)
                        .into(binding.imageBrand)
                    binding.llParent.setOnClickListener { view ->
                        run {
                            if (it.listProduct.isNullOrEmpty()) {
                                repository?.getModel(it.id)
                                    ?.applyScheduler()
                                    ?.subscribe(
                                        {
                                            item.listProduct = it.data
                                            if (context != null) {
                                                var popupWindow =
                                                    PopupWindow(binding.llParent.context)
                                                layout = LinearLayout(context)
                                                layout!!.orientation = LinearLayout.VERTICAL
                                                if (!item.listProduct.isNullOrEmpty()) {
                                                    for (product in item.listProduct!!) {
                                                        var textView = TextView(context)
                                                        textView.text = product.name
                                                        layout!!.addView(textView)
                                                    }
                                                }
                                                popupWindow.contentView = layout
                                                popupWindow.showAsDropDown(binding.llParent)
                                            }
                                        }, {
                                            it.printStackTrace()
                                        }
                                    )
                            }
                        }
                    }
                }
            }
        }
    }



    /**
     * Get item count
     *
     * @return items count
     */
    override fun getItemCount(): Int {
        return currentList.size
    }

    /**
     * Get view type
     *
     * @param position
     * @return network/item type
     */
    override fun getItemViewType(position: Int): Int {
        return Constant.LIST_ITEM_TYPE
    }

    companion object {
        class ListDiffCallback : DiffUtil.ItemCallback<Brand>() {
            override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
                return oldItem == newItem
            }
        }
    }
}