package vn.hexagon.vietnhat.ui.list.phone

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_fone_search.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.fone.Brand
import vn.hexagon.vietnhat.data.model.fone.ProductModel
import vn.hexagon.vietnhat.databinding.FragmentFoneSearchBinding
import javax.inject.Inject

class FoneSearchFragment : MVVMBaseFragment<FragmentFoneSearchBinding, FoneSearchViewModel>(),
    View.OnClickListener {

    // View model
    private lateinit var foneSearchViewModel: FoneSearchViewModel

    // Shared Preference
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // UserId
    var userId: String? = Constant.BLANK

    //Search type
    var type: String = ""

    var brandAdapter: ArrayAdapter<String>? = null
    var modelAdapter: ArrayAdapter<String>? = null

    var brandListString = arrayListOf<String>()
    var modelListString = arrayListOf<String>()

    var selectedBrand: Brand? = null
    var selectedModel: ProductModel? = null

    override fun getBaseViewModel(): FoneSearchViewModel {
        foneSearchViewModel =
            ViewModelProviders.of(this, viewModelFactory)[FoneSearchViewModel::class.java]
        return foneSearchViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun initData(argument: Bundle?) {
        userId = sharedPreferences.getString(
            getString(R.string.variable_local_user_id),
            Constant.BLANK
        )
        foneSearchViewModel.getBrand(userId!!)
        argument?.let {
            type = FoneSearchFragmentArgs.fromBundle(it).type
        }
    }

    override fun isShowActionBar(): View? = SimpleActionBar(context).apply {
        leftButtonResource = R.drawable.ic_arrow_back_primary
        simpleTitleText = "Tìm kiếm"
        leftButtonVisible = true
        rightButtonVisible = false
        leftActionBarButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_fone_search

    override fun initView() {

        foneSearchViewModel.foneBrandResponse.observe(this, Observer { response ->
            for (item: Brand in response.data) {
                brandListString.add(item.name)
            }
            brandAdapter =
                ArrayAdapter(context!!, R.layout.item_spinner_phone_search, brandListString)
            spinnerBrand.adapter = brandAdapter
            spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    foneSearchViewModel.foneBrandResponse.value?.data?.let {
                        if (!it.isEmpty() && it.size > position) {
                            selectedBrand = it[position]
                            foneSearchViewModel.getModel(selectedBrand?.id)
                        }
                    }
                }
            }
        })


        foneSearchViewModel.foneModelResponse.observe(this, Observer { response ->
            modelListString = arrayListOf()
            response.data?.let {
                for (item: ProductModel? in response.data) {
                    item?.let { modelListString.add(it.name.toString()) }
                }
                modelAdapter =
                    ArrayAdapter(context!!, R.layout.item_spinner_phone_search, modelListString)
                spinnerModel.adapter = modelAdapter
                spinnerModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        foneSearchViewModel.foneModelResponse.value?.data?.let {
                            if (!it.isEmpty() && it.size > position) {
                                selectedModel = it[position]
                            }
                        }
                    }
                }
            }
        })

    }

    override fun initAction() {
        buttonSearch.setOnClickListener(this)
        buttonReset.setOnClickListener {
            when (type) {
                "ARG_TYPE_PHONE_HOME" -> {
                    var action =
                        FoneSearchFragmentDirections.actionFoneSearchFragmentToFoneHomeFragment(
                            "",
                            ""
                        )
                    findNavController().navigate(action)
                }
                "ARG_TYPE_PHONE_LIST" -> {
                    var action =
                        FoneSearchFragmentDirections.actionFoneSearchFragmentToPhoneListFragment(
                            "",
                            ""
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onClick(v: View?) {

        when (type) {
            "ARG_TYPE_PHONE_HOME" -> {
                var action =
                    FoneSearchFragmentDirections.actionFoneSearchFragmentToFoneHomeFragment(
                        selectedBrand?.id,
                        selectedModel?.id
                    )
                findNavController().navigate(action)
            }
            "ARG_TYPE_PHONE_LIST" -> {
                var action =
                    FoneSearchFragmentDirections.actionFoneSearchFragmentToPhoneListFragment(
                        selectedBrand?.id,
                        selectedModel?.id
                    )
                findNavController().navigate(action)
            }
        }
    }
}