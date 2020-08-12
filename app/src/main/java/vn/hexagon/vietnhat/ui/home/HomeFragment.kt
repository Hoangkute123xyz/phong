package vn.hexagon.vietnhat.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.data.model.banner.BannerResponse
import vn.hexagon.vietnhat.databinding.FragmentHomeBinding
import vn.hexagon.vietnhat.ui.MainActivity
import vn.hexagon.vietnhat.ui.foneplace.FonePlaceActivity
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.scheduleAtFixedRate

/**
 *
//                       _ooOoo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                       O\ = /O
//                   ____/`---'\____
//                 .   ' \\| |// `    .
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
 * Create on：2019-08-19
 * =====================================================
 */
class HomeFragment : MVVMBaseFragment<FragmentHomeBinding, HomeViewModel>() {

    // Get icon menu for array
    private val menuIconList = listOf(
        R.drawable.newspaper,
        R.drawable.ic_hotro,
        R.drawable.ic_item_social,
        R.drawable.ic_sieuthi,
        R.drawable.ic_nhahang,
        R.drawable.ic_giaitri,
        R.drawable.ic_dulich,
        R.drawable.ic_partment_house,
        R.drawable.ic_vieclam,
        R.drawable.ic_giaohang,
        R.drawable.ic_visa,
        R.drawable.ic_fonehouse,
        R.drawable.ic_fonehouse
    )
    // Preference
    @Inject
    lateinit var sharedPref: SharedPreferences
    // Column number
    private val NUMB = 4
    private lateinit var pagerAdapter: InfinityPagerAdapter

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun isShowBottomNavigation(): Boolean = true

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        visibility = View.GONE
        simpleTitleText = getString(R.string.app_name)
        leftButtonVisible = false
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun getBaseViewModel(): HomeViewModel {
        homeViewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
        return homeViewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initData(argument: Bundle?) {
    }

    override fun initView() {
        homeViewModel.getBanner()
        val menuList: Array<String> = resources.getStringArray(R.array.homeCircleMenu)
        activity?.let { context ->
            context.mainBottomNavigation!!.visibility = View.VISIBLE
            homeGridMenu.layoutManager = CustomGridLayoutManager(context, NUMB)
            homeGridMenu.setHasFixedSize(true)
            homeGridMenu.adapter =
                HomeGridMenuAdapter(context, menuList, menuIconList, this@HomeFragment::onItemClick)
        }
        setUpCurrentLocation()
        // Setup banner adapter
        setUpBannerAdapter()
    }

    private fun setUpCurrentLocation() {
        homeViewModel.locationResponse.observe(viewLifecycleOwner, Observer<Location> {
            (activity as MainActivity).mainViewModel.currentLocation = it
        })
        activity?.let {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it)
            permissionUtils!!.request(
                permissions =
                *arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                callback = { areGrantedAll, deniedPermissions ->
                    run {
                        if (areGrantedAll) {
                            homeViewModel.getCurrentLocation(fusedLocationProviderClient)
                        }
                    }
                })
        }
    }

    /**
     * Setup infinity pager adapter
     *
     */
    private fun setUpBannerAdapter() {
        // Init pager adapter
        pagerAdapter = InfinityPagerAdapter()
        // Get banner response
        homeViewModel.bannerResponse.observe(viewLifecycleOwner,
            Observer<BannerResponse> { response ->
                if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    DebugLog.e("SIZE: " + response?.data?.size.toString())
                    if (response.data.size > 0) {
                        pagerAdapter.setItem(response.data)
                        // Set up banner list with pager adapter
                        homeInfinityPager.adapter = pagerAdapter
                        // Display indicator
                        displayIndicator(
                            homeInfinityIndicator,
                            homeInfinityPager,
                            pagerAdapter.itemCount
                        )
                        // Active slide show
                        sliderLoop(homeInfinityPager, pagerAdapter.itemCount)
                    }
                }
            })
    }

    override fun initAction() {
    }

    /**
     * Create and display indicators
     * @param targetArea Indicator layout
     * @param pager ViewPager2
     * @param indicatorCnt Adapter.count
     */
    private fun displayIndicator(targetArea: LinearLayout, pager: ViewPager2, indicatorCnt: Int) {
        // Item less than 1 can not display as pager slider
//    if (indicatorCnt < 1) return
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
        // Active on first index
        indicator[0]?.setImageDrawable(
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
     * Scroll page by time INTERVAL
     * @param pager ViewPager2
     * @param indicatorCnt pagerAdapter.count
     */
    private fun sliderLoop(pager: ViewPager2, indicatorCnt: Int) {
        // At least 2 pages exist to trigger slider
//    if (indicatorCnt < 2) return
        // create a daemon thread
        val timer = Timer("schedule", true)
        // schedule a single event by interval
        timer.scheduleAtFixedRate(2000, 2000) {
            activity?.runOnUiThread {
                if (pager.currentItem < indicatorCnt - 1) {
                    pager.currentItem = pager.currentItem + 1
                } else {
                    pager.currentItem = 0
                }
            }
        }

    }

    /**
     * Custom GridLayoutManager to control scrolling of recyclerView
     * @param context Context
     * @param spanCount number of columns
     */
    inner class CustomGridLayoutManager(context: Context, spanCount: Int) :
        GridLayoutManager(context, spanCount) {
        // Disable scrolling of recyclerView
        override fun canScrollVertically(): Boolean {
            return false
        }
    }

    /**
     * Handle onClick item Home menu
     */
    private fun onItemClick(position: Int) {
        when (position) {
           3 -> {
                findNavController().navigate(R.id.martListFragment)
            }
            8 -> {
                findNavController().navigate(R.id.jobListFragment)
            }
            5 -> {
                findNavController().navigate(R.id.translatorListFragment)
            }
            0 -> {
                findNavController().navigate(R.id.newsListFragment)
            }
            6 -> {
                findNavController().navigate(R.id.travelListFragment)
            }
            7 -> {
                findNavController().navigate(R.id.carListFragment)
            }
            4 -> {
                findNavController().navigate(R.id.restaurantListFragment)
            }
            11 -> /*{findNavController().navigate(R.id.phoneListFragment)}*/ {
                var intent = Intent(context, FonePlaceActivity::class.java)
                startActivity(intent)
            }
            10 -> {
                findNavController().navigate(R.id.visaListFragment)
            }
            9 -> {
                findNavController().navigate(R.id.deliverListFragment)
            }
            1 -> {
                findNavController().navigate(R.id.supportListFragment)
            }
            12 -> {
                findNavController().navigate(R.id.foneGiftListFragment)
            }
            2 -> {
                findNavController().navigate(R.id.communityDetailFragment)
            }

        }
    }

}