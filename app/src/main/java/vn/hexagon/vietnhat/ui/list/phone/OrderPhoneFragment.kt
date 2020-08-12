package vn.hexagon.vietnhat.ui.list.phone

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_order_phone.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.TimeRange
import vn.hexagon.vietnhat.databinding.FragmentOrderPhoneBinding
import vn.hexagon.vietnhat.ui.list.phone.time.TimeRangeDialog
import javax.inject.Inject

class OrderPhoneFragment : MVVMBaseFragment<FragmentOrderPhoneBinding, FoneHouseDetailViewModel>(),
    View.OnClickListener {


    // View model
    private lateinit var foneHouseDetailViewModel: FoneHouseDetailViewModel
    private var userId: String? = Constant.BLANK
    private var timeRangeList = arrayListOf<TimeRange>()
    private var selectedTimeRange: TimeRange? = null

    // Shared Preference
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun getBaseViewModel(): FoneHouseDetailViewModel {
        foneHouseDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory)[FoneHouseDetailViewModel::class.java]
        return foneHouseDetailViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun initData(argument: Bundle?) {
        userId = sharedPreferences.getString(
            getString(R.string.variable_local_user_id),
            Constant.BLANK
        )
    }

    override fun isShowActionBar(): View? = SimpleActionBar(context).apply {
        simpleTitleText = "Thông tin mua hàng"
        simpleTitleColor = R.color.colorPrimary
        leftButtonResource = R.drawable.ic_arrow_back_primary
        leftButtonVisible = true
        rightButtonVisible = false
        leftActionBarButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_order_phone

    override fun initView() {
        foneHouseDetailViewModel.getTimeRange()
        foneHouseDetailViewModel.timeRangeResponse.observe(this, Observer { response ->
            response.data?.let {
                timeRangeList = it as ArrayList<TimeRange>
                tvEditTakeTime.setOnClickListener(this)
            }
        })

        foneHouseDetailViewModel.bookingResponse.observe(this, Observer { response ->
            if (response.errorId == 200) {
                Toast.makeText(context, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(context, "Đặt hàng thất bại, vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
            }
        })
        buttonSend.setOnClickListener {
            if (validateData()) {
                selectedTimeRange?.id?.let { timeRangeID ->
                    foneHouseDetailViewModel.postOrder(
                        userId!!,
                        editName.text.toString(),
                        editPhone.text.toString(),
                        editAddress.text.toString(),
                        editEmail.text.toString(),
                        editProductCode.text.toString(),
                        editPrice.text.toString(),
                        timeRangeID
                    )
                }
            }
        }
    }

    private fun validateData(): Boolean {
        if (editName.text.toString().isBlank()) {
            Toast.makeText(context, "Vui lòng nhập vào tên của bạn", Toast.LENGTH_SHORT).show()
            editName.requestFocus()
            return false
        }
        if (editAddress.text.toString().isBlank()) {
            editAddress.requestFocus()
            Toast.makeText(context, "Vui lòng nhập vào địa chỉ của bạn", Toast.LENGTH_SHORT).show()
            return false
        }
        if (editProductCode.text.toString().isBlank()) {
            editProductCode.requestFocus()
            Toast.makeText(context, "Vui lòng nhập vào mã sản phẩm", Toast.LENGTH_SHORT).show()
            return false
        }
        if (editPrice.text.toString().isBlank()) {
            editPrice.requestFocus()
            Toast.makeText(context, "Vui lòng nhập vào giá sản phẩm", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedTimeRange == null) {
            Toast.makeText(
                context,
                "Vui lòng chọn khoảng thời gian nhận sản phẩm",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    override fun initAction() {

    }

    override fun onClick(v: View?) {
        val timeRangeDialog = TimeRangeDialog.newInstance(timeRangeList)
        timeRangeDialog.setOnTimeRangeClickListener {
            tvEditTakeTime.text = String.format("%s-%s", it.timeStart, it.timeEnd)
            selectedTimeRange = it
        }
        fragmentManager?.let { timeRangeDialog.show(it, TimeRangeDialog.TAG) }
    }
}