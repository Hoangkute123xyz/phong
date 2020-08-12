package vn.hexagon.vietnhat.ui.example

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.databinding.FragmentExampleUserBinding

/**
 * Created by NhamVD on 2019-08-03.
 */
class ExampleFragment : MVVMBaseFragment<FragmentExampleUserBinding, ExampleFragmentViewModel>() {
    override fun isShowActionBar(): View? = SimpleActionBar(activity)

    override fun isActionBarOverlap(): Boolean = false

    private lateinit var exampleFragmentViewModel: ExampleFragmentViewModel

    override fun getBaseViewModel(): ExampleFragmentViewModel {
        exampleFragmentViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ExampleFragmentViewModel::class.java)
        return exampleFragmentViewModel
    }
    override fun isShowBottomNavigation(): Boolean = false

    override fun initData(argument: Bundle?) {

    }

    override fun getBindingVariable(): Int = BR.viewmodel

    override fun getLayoutId(): Int = R.layout.fragment_example_user

    override fun initView() {
        val dataBinding = getBaseViewDataBinding()
        dataBinding.userid = "JakeWharton"
        dataBinding.userdata = exampleFragmentViewModel.userExample
    }

    override fun initAction() {

    }
}