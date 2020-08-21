package vn.hexagon.vietnhat.ui.list.phone

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_phone_list.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.view.EndlessScrollingRecycler
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.ListFoneHouseResponse
import vn.hexagon.vietnhat.data.model.service.ListPostResponse
import vn.hexagon.vietnhat.databinding.FragmentPhoneListBinding
import vn.hexagon.vietnhat.ui.dialog.search.DialogSearchClickListener
import vn.hexagon.vietnhat.ui.list.PostListViewModel
import javax.inject.Inject

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
class PhoneListFragment : MVVMBaseFragment<FragmentPhoneListBinding, PostListViewModel>(),
    DialogSearchClickListener, View.OnTouchListener {
    // View model
    private lateinit var postListViewModel: PostListViewModel

    // Shared Preference
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // UserId
    var userId: String? = Constant.BLANK

    //Product id
    var productID: String? = ""

    //Model id
    var modelID: String? = ""

    var phoneAdapter: FoneHouseListAdapter? = null

    override fun getBaseViewModel(): PostListViewModel {
        postListViewModel =
            ViewModelProviders.of(this, viewModelFactory)[PostListViewModel::class.java]
        return postListViewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun initData(argument: Bundle?) {
        userId = sharedPreferences.getString(
            getString(R.string.variable_local_user_id),
            Constant.BLANK
        )
        postListViewModel.userId = userId.toString()
        argument?.let {
            productID = PhoneListFragmentArgs.fromBundle(it).productID
            modelID = PhoneListFragmentArgs.fromBundle(it).modelID
        }
        if (!TextUtils.isEmpty(productID) && !TextUtils.isEmpty(modelID)) {
            searchData(productID!!, modelID!!, Constant.POST_PER_PAGE)
        } else {
            loadData(Constant.POST_PER_PAGE)
        }

        userId?.let { postListViewModel.getBrand(it) }

    }

    private fun searchData(productID: String, modelID: String, postPerPage: Int) {
        postListViewModel.getFilterPhone(productID, modelID, 0, postPerPage)

    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        simpleTitleText = getString(R.string.create_post_phone)
        leftButtonVisible = true
        rightButtonVisible = false
        rightButtonResource = R.drawable.ic_search
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftActionBarButton.setOnClickListener {
            activity?.finish()
        }
        rightActionBarButton.setOnClickListener {
            var action =
                PhoneListFragmentDirections.actionPhoneListFragmentToFoneSearchFragment("ARG_TYPE_PHONE_LIST")
            findNavController().navigate(action)
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_phone_list

    override fun initView() {

        view?.setOnTouchListener(this)


        // init adapter
        phoneAdapter = FoneHouseListAdapter(postListViewModel, ::onItemClick)
//        val filterListAdapter = PhoneFilterAdapter()
        // Refresh layout
        phoneListRefresher.setOnRefreshListener {
            if (!TextUtils.isEmpty(productID) && !TextUtils.isEmpty(modelID)) {
                searchData(productID!!, modelID!!, Constant.POST_PER_PAGE)
            } else {
                loadData(Constant.POST_PER_PAGE)
            }
        }

        activity?.let { context ->
            phoneRecyclerView.apply {
                setHasFixedSize(true)
                val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                layoutManager = gridLayoutManager
                adapter = phoneAdapter

                addOnScrollListener(object : EndlessScrollingRecycler(gridLayoutManager) {
                    override fun onLoadMore(numberPost: Int) {
                        DebugLog.e("COCA: $numberPost")
                        if (!TextUtils.isEmpty(productID) && !TextUtils.isEmpty(modelID)) {
                            searchData(productID!!, modelID!!, numberPost * 20)
                        } else {
                            loadData(numberPost * 20)
                        }
                    }
                })


            }


            rcFilter.apply {
                setHasFixedSize(true)
                val linearLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                layoutManager = linearLayoutManager
            }

            postListViewModel.foneSearchResponse.observe(this, Observer { response ->
                phoneAdapter!!.submitList(response.data)
                getResponse(response)
            })

        }

        postListViewModel.foneBrandResponse.observe(this, Observer { response ->
            val phoneFilterAdapter =
                PhoneFilterAdapter(response.data, context, postListViewModel.repository)

            phoneFilterAdapter.setOnPhoneFilterClickListener { brandID, modelID ->
                postListViewModel.getFilterPhone(brandID, modelID, 0, Constant.SUCCESS_CODE)
            }

            rcFilter.adapter = phoneFilterAdapter
        })

        // Response data
        postListViewModel.foneListResponse.observe(this, Observer { response ->
            DebugLog.e("SIZE: ${response.data.size}")

            phoneAdapter!!.submitList(response.data)
            phoneAdapter!!.setList(response.data)
            getResponse(response)

            searchViewListView!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    phoneAdapter!!.filter.filter(charSequence.toString())
                }

                override fun afterTextChanged(editable: Editable) {}
            })

        })

    }

    /**
     * Response and handle event after add fav
     *
     * @param response addFav
     */
    private fun removeFavResponse(response: ListPostResponse) {
        if (response.errorId == Constant.RESPOND_CD) {
            DebugLog.e(response.message)
        } else {
            showAlertDialog("Remove Favourite", response.message, "OK")
        }
    }

    /**
     * Response and handle event after remove fav
     *
     * @param response removeFav
     */
    private fun addFavResponse(response: ListPostResponse) {
        if (response.errorId == Constant.RESPOND_CD) {
            DebugLog.e(response.message)
        } else {
            showAlertDialog("Add Favourite", response.message, "OK")
        }
    }

    /**
     * Fetch data list
     *
     */
    private fun loadData(numberPost: Int) {
        postListViewModel.getFoneHouse(
            0,
            numberPost
        )
    }

    /**
     * Response from server after commit post
     *
     * @param response
     */
    private fun getResponse(response: ListFoneHouseResponse) {
        phoneListRefresher.isRefreshing = false
        if (response.errorId == Constant.RESPOND_CD) {
            DebugLog.e("Fetch List Success: ${response.errorId}")
        } else {
            showAlertDialog("Error", response.message, "OK")
        }
    }

    override fun initAction() {
    }

    /**
     * Request search result by condition
     *
     * @param title
     * @param address
     * @param subjectId
     */
    override fun searchTriggered(
        title: String?,
        address: String?,
        subjectId: String?
    ) {
        userId?.let {
            postListViewModel.getSearchResultCommon(
                it,
                Constant.PHONE_SERVICE_ID,
                title,
                address,
                Constant.INDEX,
                Constant.POST_PER_PAGE
            )
        }
    }

    /**
     * Go to detail item
     *
     * @param userId
     * @param postId
     */
    private fun onItemClick(userId: String, postId: String) {
        var action = PhoneListFragmentDirections.actionPhoneListFragmentToFoneHouseDetailFragment(
            userId,
            postId
        )
        findNavController().navigate(action)

    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.e("View", v?.id.toString())
        return false
    }


}