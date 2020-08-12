package vn.hexagon.vietnhat.ui.list.phone

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_product_guarantee.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.databinding.FragmentProductGuaranteeBinding
import vn.hexagon.vietnhat.ui.foneplace.FonePlaceViewModel
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
class GuaranteeListFragment :
    MVVMBaseFragment<FragmentProductGuaranteeBinding, FonePlaceViewModel>() {
    // View model
    private lateinit var viewModel: FonePlaceViewModel

    // Shared Preference
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // UserId
    var userId: String? = Constant.BLANK

    override fun getBaseViewModel(): FonePlaceViewModel {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[FonePlaceViewModel::class.java]
        return viewModel
    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun initData(argument: Bundle?) {
        userId = sharedPreferences.getString(
            getString(R.string.variable_local_user_id),
            Constant.BLANK
        )
        viewModel.warrantResponse.observe(viewLifecycleOwner, Observer { response ->
            response.data?.let {
                scrollContent.visibility = View.VISIBLE
                var a = getBaseViewDataBinding()
                a.data = response
            }
        })
    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        simpleTitleText = "Kiểm tra bảo hành"
        leftButtonVisible = true
        rightButtonVisible = false
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftActionBarButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_product_guarantee

    override fun initView() {

    }

    override fun initAction() {
        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                userId?.let { viewModel.getWarrant(it, edtSearch.text.toString())
                hideKeyBoard()
                }
                true
            } else {
                false
            }
        }
    }
}