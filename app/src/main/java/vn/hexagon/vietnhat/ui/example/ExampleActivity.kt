package vn.hexagon.vietnhat.ui.example

import androidx.lifecycle.ViewModelProviders
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseActivity
import vn.hexagon.vietnhat.databinding.ActivityExampleUserBinding

/**
 * Created by NhamVD on 2019-08-03.
 */
class ExampleActivity : MVVMBaseActivity<ActivityExampleUserBinding, ExampleActivityViewModel>() {

  private lateinit var exampleActivityViewModel: ExampleActivityViewModel

  override fun getBaseViewModel(): ExampleActivityViewModel {
    exampleActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
      .get(ExampleActivityViewModel::class.java)
    return exampleActivityViewModel
  }

  override fun getBindingVariable(): Int = BR.viewmodel

  override fun getLayoutId(): Int = R.layout.activity_example_user

  override fun initView() {

  }

  override fun initAction() {}
}