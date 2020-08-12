package vn.hexagon.vietnhat.ui.comment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_phone_gift_comment.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.databinding.FragmentPhoneGiftCommentBinding
import vn.hexagon.vietnhat.ui.favourite.GiftFavouriteAdapter
import javax.inject.Inject

class PhoneGiftCommentFragment :
    MVVMBaseFragment<FragmentPhoneGiftCommentBinding, PhoneGiftViewModel>() {

    // View model
    private lateinit var phoneGiftViewModel: PhoneGiftViewModel

    //Action bar
    var actionBar: SimpleActionBar? = null

    //Gift id
    var giftID: String? = Constant.BLANK

    //Type to show Content
    var type: String? = null

    //Comment Adapter
    var commentAdapter: CommentPhoneGiftAdapter? = null

    //Favorite Adapter
    var favouriteAdapter: GiftFavouriteAdapter? = null

    //Vertical layout manager
    var linearLayoutManager: LinearLayoutManager? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun getBaseViewModel(): PhoneGiftViewModel {
        phoneGiftViewModel =
            ViewModelProviders.of(this, viewModelFactory)[PhoneGiftViewModel::class.java]
        return phoneGiftViewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun initData(argument: Bundle?) {
        argument?.let {
            giftID = PhoneGiftCommentFragmentArgs.fromBundle(it).id
            type = PhoneGiftCommentFragmentArgs.fromBundle(it).type
            commentRecyclerView.setHasFixedSize(true)
            linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            commentRecyclerView.layoutManager = linearLayoutManager
            when (type) {
                "TYPE_PHONE_GIFT_COMMENT" -> {
                    actionBar?.simpleTitleText = "Bình luận"
                    phoneGiftViewModel.getPhoneGiftComment(giftID)
                    tvSendBtn.visibility = View.VISIBLE
                    editContent.visibility = View.VISIBLE
                    commentAdapter = CommentPhoneGiftAdapter(phoneGiftViewModel)
                    commentRecyclerView.adapter = commentAdapter
                    tvSendBtn.setOnClickListener {
                        editContent.text?.let {
                            phoneGiftViewModel.postCommentPhoneGift( it.toString(),giftID!!,"129")
                        }
                    }
                }

                "TYPE_PHONE_HOUSE_COMMENT" -> {
                    actionBar?.simpleTitleText = "Bình luận"
                    phoneGiftViewModel.getPhoneHouseComment(giftID)
                    tvSendBtn.visibility = View.VISIBLE
                    editContent.visibility = View.VISIBLE
                    commentAdapter = CommentPhoneGiftAdapter(phoneGiftViewModel)
                    commentRecyclerView.adapter = commentAdapter
                    tvSendBtn.setOnClickListener {
                        editContent.text?.let {
                            phoneGiftViewModel.postCommentPhoneHouse(it.toString(),giftID!!,"129")
                        }
                    }
                }

                "TYPE_PHONE_GIFT_LIKE" -> {
                    actionBar?.simpleTitleText = "Danh sách quan tâm"
                    phoneGiftViewModel.getPhoneGiftFavourite(giftID)
                    tvSendBtn.visibility = View.GONE
                    editContent.visibility = View.GONE
                    favouriteAdapter = GiftFavouriteAdapter()
                    commentRecyclerView.adapter = favouriteAdapter
                }

                "TYPE_PHONE_HOUSE_FAVORITE" -> {
                    actionBar?.simpleTitleText = "Danh sách người yêu thích"
                    phoneGiftViewModel.getPhoneHouseFavorite(giftID)
                    phoneGiftViewModel.listFavoritePhoneResponse.observe(this, Observer {
                        favouriteAdapter = GiftFavouriteAdapter()
                        favouriteAdapter!!.submitList(it.data)
                        commentRecyclerView.adapter = favouriteAdapter
                    })
                    tvSendBtn.visibility = View.GONE
                    editContent.visibility = View.GONE

                }
            }
        }


    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        leftButtonVisible = true
        simpleTitleColor = R.color.colorPrimary
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftActionBarButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_phone_gift_comment

    override fun initView() {
        actionBar = baseActionBar as SimpleActionBar


    }

    override fun initAction() {

    }

}