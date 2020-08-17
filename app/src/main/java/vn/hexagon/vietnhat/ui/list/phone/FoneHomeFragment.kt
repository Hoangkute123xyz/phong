package vn.hexagon.vietnhat.ui.list.phone

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_fone_home.*
import kotlinx.android.synthetic.main.fragment_news_list.*
import kotlinx.android.synthetic.main.fragment_phone_list.*
import kotlinx.android.synthetic.main.fragment_phone_list.phoneListRefresher
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.view.EndlessScrollingRecycler
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.ListFoneHouseResponse
import vn.hexagon.vietnhat.data.model.service.ListPostResponse
import vn.hexagon.vietnhat.databinding.FragmentFoneHomeBinding
import vn.hexagon.vietnhat.ui.chat.community.CommunityDetailFragmentDirections
import vn.hexagon.vietnhat.ui.dialog.search.DialogSearchClickListener
import vn.hexagon.vietnhat.ui.list.PostListViewModel
import java.util.ArrayList
import javax.inject.Inject

class FoneHomeFragment : MVVMBaseFragment<FragmentFoneHomeBinding, PostListViewModel>(),
    DialogSearchClickListener {

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

    var phoneAdapter: PhoneListAdapter? = null
    var allProductAdapter: FoneHouseListAdapter? = null


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
        try {
            argument?.let {
                productID = FoneHomeFragmentArgs.fromBundle(it).productID
                modelID = FoneHomeFragmentArgs.fromBundle(it).modelID
            }
        } catch (e: Exception) {
            e.printStackTrace()
            productID = ""
            modelID = ""
        }
        if (!TextUtils.isEmpty(productID) && !TextUtils.isEmpty(modelID)) {
            searchData(productID!!, modelID!!, Constant.POST_PER_PAGE)
        } else {
            loadData(Constant.POST_PER_PAGE)
        }
    }


    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        simpleTitleText = "FoneHouse"
        leftButtonVisible = true
        rightButtonVisible = false
        rightButtonResource = R.drawable.ic_search
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftActionBarButton.setOnClickListener {
            activity?.finish()
        }
        rightActionBarButton.setOnClickListener {
            val action =
                FoneHomeFragmentDirections.actionFoneHomeFragmentToFoneSearchFragment("ARG_TYPE_PHONE_HOME")
            findNavController().navigate(action)
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_fone_home

    override fun initView() {

        imageViewBannerFoneHome.setOnClickListener {
            val list = ArrayList<String>()
            list.add(R.drawable.img_guide.toString())
//            val position: Int = 0
            onClickImg(list, 0)
        }

        allProductAdapter = FoneHouseListAdapter(postListViewModel, ::onItemClick)

        allProductListRefresher.setOnRefreshListener {
            if (!TextUtils.isEmpty(productID) && !TextUtils.isEmpty(modelID)) {
                searchData(productID!!, modelID!!, Constant.POST_PER_PAGE)
            } else {
                loadData(Constant.POST_PER_PAGE)
            }
        }

        activity?.let { context ->
            allProductRecyclerView.apply {
                setHasFixedSize(true)
                val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
                layoutManager = gridLayoutManager
                adapter = allProductAdapter
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

            postListViewModel.foneSearchResponse.observe(this, Observer { response ->
                allProductAdapter!!.submitList(response.data)
                getResponse(response)
            })

        }


        // Response data
        postListViewModel.foneListResponse.observe(this, Observer { response ->
            DebugLog.e("SIZE all product: ${response.data.size}")
            allProductAdapter!!.submitList(response.data)
            getResponse(response)
        })

    }

    fun onClickImg(list: ArrayList<String>, position: Int) {
        val listImg = arrayOfNulls<String>(list.size)
        list.toArray(listImg)

        val action =
            FoneHomeFragmentDirections.actionFoneHomeFragmentToZoomFragment(
                listImg,
                position
            )
        findNavController().navigate(action)
    }

    private fun onClickFav(b: Boolean) {
        val message = if (isAdded) {
            getString(R.string.add_favourite_message)
        } else {
            getString(R.string.remove_favourite_message)
        }
        Snackbar.make(phoneListParent, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Response from server after commit post
     *
     * @param response
     */
    private fun getResponse(response: ListFoneHouseResponse) {
        allProductListRefresher.isRefreshing = false
        if (response.errorId == Constant.RESPOND_CD) {
            DebugLog.e("Fetch List Success: ${response.errorId}")
        } else {
            showAlertDialog("Error", response.message, "OK")
        }
    }

    private fun loadData(numberPost: Int) {
        userId?.let {
            postListViewModel.getDataListPost(
                it,
                Constant.PHONE_SERVICE_ID,
                0,
                numberPost,
                context!!
            )
        }

        postListViewModel.getFoneHouse(
            0,
            numberPost
        )

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

        Toast.makeText(context, "Xem chi tiết ở danh sách sản phẩm!", Toast.LENGTH_LONG).show()
//        val action =
//            FoneHomeFragmentDirections.actionFoneHomeFragmentToPostDetailFragment(userId, postId)
//        findNavController().navigate(action)

//        var action = PhoneListFragmentDirections.actionPhoneListFragmentToFoneHouseDetailFragment(
//            userId,
//            postId
//        )
//        findNavController().navigate(action)
    }

    private fun searchData(productID: String, modelID: String, numberPost: Int) {
        postListViewModel.searchPhoneHouse(productID, modelID, numberPost)
    }
}