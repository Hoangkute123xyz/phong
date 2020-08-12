package vn.hexagon.vietnhat.ui.foneplace

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_fone_place.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseActivity
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.databinding.ActivityFonePlaceBinding
import javax.inject.Inject

class FonePlaceActivity : MVVMBaseActivity<ActivityFonePlaceBinding, FonePlaceViewModel>() {

    private var navController: NavController? = null

    @Inject
    lateinit var fonePlaceViewModel: FonePlaceViewModel
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    // UserId
    var userId: String? = Constant.BLANK

    override fun getBaseViewModel(): FonePlaceViewModel {
        fonePlaceViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FonePlaceViewModel::class.java)
        return fonePlaceViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_fone_place

    override fun initView() {
        userId =
            sharedPreferences.getString(getString(R.string.variable_local_user_id), Constant.BLANK)
        foneBottomNavigation.apply {
            itemIconTintList = null
        }
        navController = findNavController(R.id.navHostFragment)
        navController?.let { navController ->
            foneBottomNavigation.setupWithNavController(navController)
        }
    }

    override fun initAction() {
        navController?.let { navController ->
            foneBottomNavigation.setOnNavigationItemSelectedListener { item ->
                hideKeyBoard()
                NavigationUI.onNavDestinationSelected(item, navController)
                false
            }
        }
    }
}