package vn.hexagon.vietnhat.ui.list.phone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_fone_house_detail.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.Fone
import vn.hexagon.vietnhat.databinding.FragmentFoneHouseDetailBinding
import vn.hexagon.vietnhat.ui.home.InfinityPagerAdapter
import java.util.*

class FoneHouseDetailFragment :
    MVVMBaseFragment<FragmentFoneHouseDetailBinding, FoneHouseDetailViewModel>() {

    // View model
    private lateinit var foneHouseDetailViewModel: FoneHouseDetailViewModel

    private lateinit var pagerAdapter: InfinityPagerAdapter

    private var productId: String? = null
    private var userId: String? = Constant.BLANK
    private var authorPhone: String? = Constant.BLANK

    override fun getBaseViewModel(): FoneHouseDetailViewModel {
        foneHouseDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory)[FoneHouseDetailViewModel::class.java]
        return foneHouseDetailViewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun initData(argument: Bundle?) {
        argument?.let {
            productId = FoneHouseDetailFragmentArgs.fromBundle(it).productID
            userId = FoneHouseDetailFragmentArgs.fromBundle(it).userId

//            Toast.makeText(context, productId, Toast.LENGTH_LONG).show()
//            Toast.makeText(context, userId, Toast.LENGTH_LONG).show()

            userId?.let { userId -> foneHouseDetailViewModel.getFoneDetail(userId, productId!!) }
        }
    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        simpleTitleText = "Chi tiết sản phẩm"
        leftButtonVisible = true
        rightButtonVisible = false
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftActionBarButton?.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_fone_house_detail

    override fun initView() {
        // Init pager adapter
//        pagerAdapter = InfinityPagerAdapter()
        var actionBar = baseActionBar as SimpleActionBar


        actionBar.leftActionBarButton?.setOnClickListener {
            findNavController().popBackStack()
        }

        tvNumberComments.setOnClickListener {
            val action =
                FoneHouseDetailFragmentDirections.actionFoneHouseDetailFragmentToPhoneGiftCommentFragment(
                    productId!!,
                    "TYPE_PHONE_HOUSE_COMMENT"
                )
            findNavController().navigate(action)
        }

        tvNumberFavourite.setOnClickListener {
            val action =
                FoneHouseDetailFragmentDirections.actionFoneHouseDetailFragmentToPhoneGiftCommentFragment(
                    productId!!,
                    "TYPE_PHONE_HOUSE_FAVORITE"
                )
            findNavController().navigate(action)
        }


        foneHouseDetailViewModel.foneDetailResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                activity?.let {
                    val pagerAdapter =
                        PagerDetailAdapter(it, ::onClickImg)
                    getResponse(response.data)
                    if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        DebugLog.e("SIZE: " + response?.data?.img?.size.toString())
                        // Image cover
                        pagerAdapter.initPager(response.data.img)
                    }
                }
            })

        tvStartCall.setOnClickListener {
            authorPhone?.let { it1 -> handleAction(Constant.REQUEST_CALL, it1) }
        }

        tvStartMessage.setOnClickListener {
            authorPhone?.let { it1 -> handleAction(Constant.REQUEST_MSG, it1) }
        }

        tvBuy.setOnClickListener {
            var action =
                FoneHouseDetailFragmentDirections.actionFoneHouseDetailFragmentToOrderPhoneFragment(
                    userId!!,
                    productId!!
                )
            findNavController().navigate(action)
        }
    }

    private fun getResponse(data: Fone) {
        authorPhone = data.contact
    }

    override fun initAction() {

    }

    /**
     * Init pager images
     *
     */
    private fun PagerDetailAdapter.initPager(list: List<String>) {
        this.setItem(list)
        // Set up banner list with pager adapter
        foneDetailInfinityPager.clipToOutline = true
        foneDetailInfinityPager.adapter = this
        // Display indicator
        displayIndicator(detailInfinityIndicator, foneDetailInfinityPager, this.itemCount)
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

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    fun onClickImg(list: ArrayList<String>, position: Int) {
        val listImg = arrayOfNulls<String>(list.size)
        list.toArray(listImg)
        val action =
            FoneHouseDetailFragmentDirections.actionFoneHouseDetailFragmentToZoomFragment(
                listImg,
                position
            )
        findNavController().navigate(action)
    }

    /**
     * Handle when user call author
     *
     * @param requestCd
     */
    private fun handleAction(requestCd: Int, authorPhone: String) {
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
}


