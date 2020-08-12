package vn.hexagon.vietnhat.ui.setting


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account_info.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_unlogin_common.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.auth.LoginResponse
import vn.hexagon.vietnhat.databinding.FragmentSettingBinding
import vn.hexagon.vietnhat.ui.dialog.PostSelectDialogFragment
import vn.hexagon.vietnhat.ui.home.HomeGridMenuAdapter
import vn.hexagon.vietnhat.ui.list.WindyConvertUtil
import javax.inject.Inject

/**
 * Create by VuNBT on 8/15/2019
 */
class SettingFragment : MVVMBaseFragment<FragmentSettingBinding, SettingViewModel>(),
  View.OnClickListener {

  // Action bar
  private val actionBar: SimpleActionBar? by lazy {
    baseActionBar as? SimpleActionBar
  }
  // Get icon menu for array
  private val menuIconList = listOf<Int>(
    R.drawable.ic_taikhoan2, R.drawable.ic_mana_post,R.drawable.contract, R.drawable.ic_naptien2,
    R.drawable.ic_thongbao2, R.drawable.ic_huongdan2, R.drawable.ic_danhgia2
  )
  // Column number
  private val NUMB = 4
  // Setting View Model
  private lateinit var settingViewModel: SettingViewModel
  // Shared Preferences
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  // UserId
  private var userId: String? = Constant.BLANK

  override fun getBaseViewModel(): SettingViewModel {
    settingViewModel = ViewModelProviders.of(this, viewModelFactory)[SettingViewModel::class.java]
    return settingViewModel
  }

  override fun getBindingVariable(): Int = BR.viewmodel

  override fun initData(argument: Bundle?) {}

  override fun isShowBottomNavigation(): Boolean = true

  override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
    simpleTitleText = getString(R.string.setting_title)
    leftButtonVisible = false
  }

  override fun isActionBarOverlap(): Boolean = false

  override fun getLayoutId(): Int = R.layout.fragment_setting

  override fun initView() {
    // Init layout by condition
    if (!isToken()) {
      processBeforeLogin()
    } else {
      processAfterLogin()
    }
  }

  /**
   * Handle layout when user not login
   *
   */
  private fun processBeforeLogin() {
    settingNotLoginArea.visibility = View.VISIBLE
    unLoginRegister.setOnClickListener(this)
    unLoginSignIn.setOnClickListener(this)
  }

  /**
   * Handle layout after user login
   *
   */
  private fun processAfterLogin() {
    // Get list menu from string array
    activity?.let { context ->
      context.mainBottomNavigation!!.visibility = View.VISIBLE
      val menuList: Array<String> = resources.getStringArray(R.array.settingCircleMenu)
      gridRecycler.apply {
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        adapter =
          SettingAdapter(context, menuList, menuIconList, this@SettingFragment::onItemClick)
      }

      // Logout process
      settingLogoutBtn.setOnClickListener {
        val bundle = bundleOf("isLogout" to true)
        // Clear token
        sharedPreferences.edit().apply {
          remove(getString(R.string.token))
          remove(getString(R.string.variable_local_user_id))
        }.also { it.apply() }
        processBeforeLogin()
      }

      userId = sharedPreferences.getString(getString(R.string.variable_local_user_id),
        Constant.BLANK)
      loadData()
      // Get response account info
      settingViewModel.userInfoResponse.observe(this, Observer { response ->
        getResponse(response)
      })
    }
  }

  override fun initAction() {}

  /**
   * Handle onClick menu
   */
  private fun onItemClick(position: Int) {
    when (position) {
      //:TODO - Implement onClick menu
      0 -> findNavController().navigate(R.id.accountInfoFragment)
      1 -> findNavController().navigate(R.id.managePostFragment)
      2 -> {
        fragmentManager?.let {
          PostSelectDialogFragment().show(it, "post")
        }
      }
      3->{}
      4 -> findNavController().navigate(R.id.notifyFragment)
      5 -> findNavController().navigate(R.id.guideListFragment)
      6 -> findNavController().navigate(R.id.rateFragment)
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

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.unLoginRegister -> findNavController().navigate(R.id.registerFragment)
      R.id.unLoginSignIn -> findNavController().navigate(R.id.loginFragment)
    }
  }

  /**
   * Get token
   *
   * @return token (true is hasToken)
   */
  private fun isToken(): Boolean {
    val token = sharedPreferences.getString(
      getString(R.string.token),
      Constant.BLANK
    )
    return token == Constant.TOKEN
  }

  private fun loadData() {
    userId?.let { userId ->
      settingViewModel.getUserInfo(userId)
    }
  }

  private fun getResponse(response: LoginResponse) {
    if (response.errorId == Constant.RESPOND_CD) {
      DebugLog.e("Fetch Account Info Success: ${response.errorId}")
      txtName.setText(response.user.name)
      txtPhone.setText(response.user.phone)
      txtEmail.setText(response.user.email)
    } else {
      showAlertDialog("Error", response.message, "OK")
    }
  }

}
