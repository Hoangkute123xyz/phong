package vn.hexagon.vietnhat.ui.detail

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_fone_gift_detail.*
import kotlinx.android.synthetic.main.fragment_fone_house_detail.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.Utils
import vn.hexagon.vietnhat.base.view.PlacesUtils
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.gift.GiftDetail
import vn.hexagon.vietnhat.data.model.post.Post
import vn.hexagon.vietnhat.databinding.FragmentFoneGiftDetailBinding
import vn.hexagon.vietnhat.ui.list.phone.FoneHouseDetailViewModel
import vn.hexagon.vietnhat.ui.list.phone.PagerDetailAdapter
import java.text.SimpleDateFormat
import javax.inject.Inject

class FoneGiftDetailFragment :
    MVVMBaseFragment<FragmentFoneGiftDetailBinding, FoneHouseDetailViewModel>(),
    View.OnClickListener, ProductDetailAdapter.OnItemClickListener {
    // Action bar
    private val actionBar: SimpleActionBar? by lazy {
        baseActionBar as? SimpleActionBar
    }

    // Account Info View Model
    private lateinit var foneHouseDetailViewModel: FoneHouseDetailViewModel

    // Author phone number
    private var authorPhone = Constant.BLANK

    // Shared preference
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // Product Adapter
    private lateinit var productAdapter: ProductDetailAdapter

    // ServiceId
    private var serviceId = Constant.BLANK

    // UserId
    private var userId: String? = Constant.BLANK

    // Post ID
    private var postId: String = Constant.BLANK


    // Is Add favourite flag
    private var mIsClick = false

    // Places
    private var mPlaces: PlacesUtils? = null

    private lateinit var postData: Post

    override fun getBaseViewModel(): FoneHouseDetailViewModel {
        foneHouseDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory)[FoneHouseDetailViewModel::class.java]
        return foneHouseDetailViewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun initData(argument: Bundle?) {
        val dataBinding = getBaseViewDataBinding()
        dataBinding.viewmodel = foneHouseDetailViewModel
        // Received postId and userId
        userId =
            sharedPreferences.getString(getString(R.string.variable_local_user_id), Constant.BLANK)
        argument?.let {
            postId = FoneGiftDetailFragmentArgs.fromBundle(it).giftID
            // Get gift info
            userId?.let { userId -> foneHouseDetailViewModel.getPhoneGiftDetail(userId, postId) }
        }
    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity)

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_fone_gift_detail

    override fun initView() {
        activity?.let { context ->
            // Places picker init
            mPlaces = PlacesUtils(context)
        }

        // Add favourite response
        foneHouseDetailViewModel.favouriteResponse.observe(this, Observer { response ->
            if (response.errorId == Constant.RESPOND_CD) {
                if (mIsClick) {
                    Snackbar.make(
                        postDetailParent,
                        getString(R.string.add_favourite_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    mIsClick = false
                }
            }
        })
        // Remove favourite response
        foneHouseDetailViewModel.unFavouriteResponse.observe(this, Observer { response ->
            if (response.errorId == Constant.RESPOND_CD) {
                if (mIsClick) {
                    Snackbar.make(
                        postDetailParent,
                        getString(R.string.remove_favourite_message),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    mIsClick = false
                }
            }
        })
        // Init recyclerView
        initRecyclerView()
        // Detail gift response
        foneHouseDetailViewModel.foneGiftDetailResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                if (response.errorId.toString() == Constant.RESPOND_CD) {
                    activity?.let { context ->
                        val pagerAdapter = PagerDetailAdapter(
                            context,
                            ::onClickImg
                        )
                        getResponse(response.giftDetail)
                        if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                            // Image cover
                            response.giftDetail?.img?.let { pagerAdapter.initPager(it) }
                        }
                        initActionBar()
                    }
                } else {
                    response.message?.let { DebugLog.e(it) }
                }
            })
        // Expand all products
        detailProductViewAllLink.setOnClickListener(this)
    }

    /**
     * Display action bar
     *
     */
    private fun initActionBar() {
        // Init action bar
        actionBar?.apply {
//            simpleTitleText = "Fonehouse Gift chi tiết" //cũ
            simpleTitleText = "Quà tặng chi tiết" //theo yêu cầu
            simpleTitleColor = R.color.colorPrimary
            leftButtonVisible = true
            leftButtonResource = R.drawable.ic_arrow_back_primary
            rightButtonVisible = false
            leftActionBarButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * Init recyclerView and product adapter
     *
     */
    private fun initRecyclerView() {
        productAdapter = ProductDetailAdapter(ArrayList())
        productAdapter.setOnItemClick(this)
        activity?.let { context ->
            detailItemRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                adapter = productAdapter
            }
        }
        // Product list response
        foneHouseDetailViewModel.foneGiftDetailResponse.observe(this, Observer { response ->
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (!response.giftDetail?.products?.isEmpty()!!) {
                    productAdapter.clearAllItem()
                    detailProductListArea.visibility = View.VISIBLE
                    // Submit product list
                    productAdapter.insertItem(response.giftDetail.products)
                    if (productAdapter.list.size > 5) {
                        detailProductViewAllLink.visibility = View.VISIBLE
                        detailProductViewAllLink.text = getString(
                            R.string.reveal_all_product_link,
                            productAdapter.list.size.toString()
                        )
                    } else {
                        detailProductViewAllLink.visibility = View.GONE
                    }
                }
            }
        })
    }

    /**
     * Display data
     *
     * @param post
     */
    private fun getResponse(post: GiftDetail?) {
        /* postData = post
         // ServiceId
         serviceId = post.serviceId
         // Is top
         postDetailTopIcon.visibility =
             if (post.isTop == Constant.TOP_CHECKED) View.VISIBLE else View.GONE*/
        // Avatar
        post?.let {
            authorPhone = it.phone!!
            postDetailAvatar.clipToOutline = true
            // Date
            postListDetailDate.text =
                getString(R.string.post_detail_author_template, it.username, convertDate(it.date!!))
            // Address
            if (it.address?.isNotEmpty()!!) {
                detailGmapBtn.visibility = View.VISIBLE
                postDetailAddress.visibility = View.VISIBLE
            } else {
                detailGmapBtn.visibility = View.GONE
                postDetailAddress.visibility = View.GONE
            }

            if (it.phone.isNullOrBlank()) {
                detailCallBtn.visibility = View.GONE
            } else {
                detailCallBtn.visibility = View.VISIBLE
            }

            tvGiftCode.text  = it.code

            // Note
            if (!it.note.isNullOrBlank()) {
                tvTitleNote.visibility = View.VISIBLE
            } else {
                tvTitleNote.visibility = View.GONE
            }
            // Favourite
            if (userId != Constant.BLANK) {
                postDetailFavBtn.visibility = View.VISIBLE
                if (post.isFavourite == 1)
                    postDetailFavBtn.setImageResource(R.drawable.ic_save_active)
                else postDetailFavBtn.setImageResource(R.drawable.ic_save)
            } else {
                postDetailFavBtn.visibility = View.GONE
            }
            postDetailFavLink.text =
                String.format(getString(R.string.like_title), it.numberFavourite)
            postDetailCmtLink.text =
                String.format(getString(R.string.comment_title_count), it.numberComment)
            // Rounded button call & SMS
            detailCallBtn.clipToOutline = true
            detailMsgBtn.clipToOutline = true
            // Handle events
            postDetailCmtLink.setOnClickListener(this)
            postDetailFavLink.setOnClickListener(this)
            detailCallBtn.setOnClickListener(this)
            detailMsgBtn.setOnClickListener(this)
            detailGmapBtn.setOnClickListener(this)
            // OnClick fav button
            var isAdded = false
            postDetailFavBtn.setOnClickListener {
                mIsClick = true
                if (post.isFavourite == 0 && !isAdded) {
                    isAdded = true
                    postDetailFavBtn.setImageResource(R.drawable.ic_save_active)
                    userId?.let { foneHouseDetailViewModel.addFavourite(it, post.id!!) }
                    post.isFavourite = 1
                } else {
                    isAdded = false
                    postDetailFavBtn.setImageResource(R.drawable.ic_save)
                    userId?.let { foneHouseDetailViewModel.removeFavourite(it, post.id!!) }
                    post.isFavourite = 0
                }
            }
        }
    }

    /**
     * Init pager images
     *
     */
    private fun PagerDetailAdapter.initPager(list: List<String>) {
        this.setItem(list)
        // Set up banner list with pager adapter
        detailInfinityPager.clipToOutline = true
        detailInfinityPager.adapter = this
        // Display indicator
        displayIndicator(homeInfinityIndicator, detailInfinityPager, this.itemCount)
    }

    /**
     * Create and display indicators
     * @param targetArea Indicator layout
     * @param pager ViewPager2
     * @param indicatorCnt Adapter.count
     */
    private fun displayIndicator(targetArea: LinearLayout, pager: ViewPager2, indicatorCnt: Int) {
        // Item less than 2 can not display as pager slider
        if (indicatorCnt < 2) return
        val indicator = arrayOfNulls<ImageView>(indicatorCnt)
        for (i in 0 until indicatorCnt) {
            indicator[i] = ImageView(activity)
            indicator[i]?.setImageDrawable(
                activity?.applicationContext?.let { context ->
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.tab_indicator_default
                    )
                }
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            targetArea.addView(indicator[i], params)
        }
        // Get last position then re-display (popBackStack case)
        val lastPosition = if (pager.currentItem != -1) pager.currentItem else 0
        // Active on first index
        indicator[lastPosition]?.setImageDrawable(
            activity?.applicationContext?.let { context ->
                ContextCompat.getDrawable(
                    context,
                    R.drawable.tab_indicator_selected
                )
            }
        )
        // Sync indicator with pager
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until indicatorCnt) {
                    indicator[i]?.setImageDrawable(
                        activity?.applicationContext?.let { context ->
                            ContextCompat
                                .getDrawable(context, R.drawable.tab_indicator_default)
                        }
                    )
                    indicator[position]?.setImageDrawable(
                        activity?.applicationContext?.let { context ->
                            ContextCompat
                                .getDrawable(context, R.drawable.tab_indicator_selected)
                        }
                    )
                }
            }
        })
    }


    /**
     * Handle onClick items
     *
     * @param v
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.postDetailCmtLink -> {
                val action =
                    FoneGiftDetailFragmentDirections.actionPostFoneGiftDetailFragmentToPhoneGiftCommentFragment(
                        postId,
                        "TYPE_PHONE_GIFT_COMMENT"
                    )
                findNavController().navigate(action)
            }
            R.id.postDetailFavLink -> {
                val action =
                    FoneGiftDetailFragmentDirections.actionPostFoneGiftDetailFragmentToPhoneGiftCommentFragment(
                        postId,
                        "TYPE_PHONE_GIFT_LIKE"
                    )
                findNavController().navigate(action)
            }
            R.id.detailCallBtn -> handleAction(Constant.REQUEST_CALL)
            R.id.detailMsgBtn -> {
                if (isToken()) {
                    val action = FoneGiftDetailFragmentDirections
                        .actionPostFoneGiftDetailFragmentToChatDetailFragment(
                            postData.userId,
                            postData.userName,
                            postData.userAvatar,
                            postData.phone
                        )
                    findNavController().navigate(action)
                } else {
                    findNavController().navigate(R.id.loginFragment)
                }
            }
            R.id.detailPostPrice -> findNavController().navigate(R.id.loginFragment)
            R.id.detailProductViewAllLink -> {
                detailProductListArea.transitionName
                productAdapter.isRevealAllItem = true
                productAdapter.notifyDataSetChanged()
                detailProductViewAllLink.visibility = View.GONE
            }
            R.id.detailGmapBtn -> openGoogleMap()
        }
    }

    /**
     * Open Google Map application to show location
     * by post's address
     *
     */
    private fun openGoogleMap() {
        // Validate address filed empty or not
        if (postDetailAddress.text.isEmpty()) {
            showAlertDialog(getString(R.string.err_msg_empty_address_map))
            return
        }
        // Query location with Google map
        mPlaces?.openGmapApplication(context, postDetailAddress.text.toString())
    }

    /**
     * Handle when user call author
     *
     * @param requestCd
     */
    private fun handleAction(requestCd: Int) {
        var intent = Intent()
        when (requestCd) {
            Constant.REQUEST_CALL -> {
                intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$authorPhone")
                }
            }
            Constant.REQUEST_MSG -> {
                intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:$authorPhone")
                }
            }
        }
        activity?.let { context ->
            if (intent.resolveActivity(context.packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun initAction() {
        buttonGetCode.setOnClickListener {
            context?.let { mContext ->
                Utils.copyContentToClipboard(tvGiftCode.text.toString(), mContext)
            }
        }
    }

    /**
     * Transition to preview image screen
     *
     * @param list
     * @param position
     */
    private fun onClickImg(list: ArrayList<String>, position: Int) {
        val listImg = arrayOfNulls<String>(list.size)
        list.toArray(listImg)
        val action =
            FoneGiftDetailFragmentDirections.actionPostFoneGiftDetailFragmentToZoomFragment(
                listImg,
                position
            )
        findNavController().navigate(action)
    }

    /**
     * Get token
     *
     * @return token (true is has token)
     */
    private fun isToken(): Boolean {
        val token = sharedPreferences.getString(
            getString(R.string.token),
            Constant.BLANK
        )
        return token == Constant.TOKEN
    }

    fun convertDate(date: String): String {
        var returnDate = ""
        if (date.isNotBlank()) {
            try {
                val simpleDateFormat = SimpleDateFormat("yyy-MM-dd hh:mm:ss")
                val calendar = simpleDateFormat.parse(date)
                val returnDateFormat = SimpleDateFormat("yyyy/MM/dd")
                returnDate = returnDateFormat.format(calendar)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return returnDate
    }

    override fun onItemClick(position: Int) {
        val listImg = arrayOfNulls<String>(1)
        listImg[0] = productAdapter.list[1].img

        val action =
            FoneGiftDetailFragmentDirections.actionPostFoneGiftDetailFragmentToZoomFragment(
                listImg,
                position
            )
        findNavController().navigate(action)
    }
}