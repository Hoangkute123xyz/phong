package vn.hexagon.vietnhat.ui.chat.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyenhoanglam.imagepicker.model.Config.CREATOR.EXTRA_IMAGES
import com.nguyenhoanglam.imagepicker.model.Config.CREATOR.RC_PICK_IMAGES
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.action_bar_base.view.*
import kotlinx.android.synthetic.main.fragment_add_group.*
import vn.hexagon.vietnhat.BR
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseFragment
import vn.hexagon.vietnhat.base.ui.SimpleActionBar
import vn.hexagon.vietnhat.base.utils.RxSearch
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.chat.FriendItem
import vn.hexagon.vietnhat.databinding.FragmentAddGroupBinding
import vn.hexagon.vietnhat.ui.chat.adapter.AddedFriendAdapter
import vn.hexagon.vietnhat.ui.chat.adapter.FriendAdapter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddGroupFragment : MVVMBaseFragment<FragmentAddGroupBinding, AddGroupViewModel>() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var verticalLayoutManager: LinearLayoutManager? = null
    private var horizontalLayoutManager: LinearLayoutManager? = null
    private var images: ArrayList<Image>? = arrayListOf()
    private var friendAdapter: FriendAdapter? = null
    var addedFriendAdapter: AddedFriendAdapter? = null
    var friendList = arrayListOf<FriendItem>()

    private lateinit var addGroupViewModel: AddGroupViewModel


    // UserId
    private val userId: String? by lazy {
        sharedPreferences.getString(
            getString(R.string.variable_local_user_id),
            Constant.BLANK
        )
    }

    private var groupID: String? = ""
    private var groupUserID: String? = ""

    companion object {
        val TAG = AddGroupFragment::class.java.canonicalName
        fun newInstance(): AddGroupFragment? {
            val args = Bundle()
            val fragment = AddGroupFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_add_group

    @SuppressLint("CheckResult")
    override fun initView() {
        verticalLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        horizontalLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)


        rvListItem?.apply {
            setHasFixedSize(true)
            layoutManager = verticalLayoutManager
        }

        addedFriendAdapter = AddedFriendAdapter(null)
        addedFriendAdapter!!.setOnItemClickListener {
            friendAdapter?.onRemoveFriend(addedFriendAdapter!!.getItem(it))
            addedFriendAdapter!!.removeFriend(it)
        }
        rvAddedItem?.apply {
            setHasFixedSize(true)
            layoutManager = horizontalLayoutManager
            adapter = addedFriendAdapter
        }

        images = ArrayList()

        groupID = arguments?.let {
            AddGroupFragmentArgs.fromBundle(it).groupID
        }
        groupUserID = arguments?.let {
            AddGroupFragmentArgs.fromBundle(it).userGroup
        }

        if (groupID.isNullOrBlank()) {
            //Group Id is null or blank -> create new group
            icAddImage.visibility = View.VISIBLE
            editGroupName.visibility = View.VISIBLE
            tvAddGroup.text = "TẠO\nNHÓM"
            (baseActionBar as SimpleActionBar).apply {
                simpleTitleText = "Tạo nhóm mới"
            }
        } else {
            //else add friend
            (baseActionBar as SimpleActionBar).apply {
                simpleTitleText = "Thêm bạn"
            }
            icAddImage.visibility = View.GONE
            editGroupName.visibility = View.GONE
            tvAddGroup.text = "Thêm\nbạn"
        }


        icAddImage!!.setOnClickListener { v: View? ->
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setRootDirectoryName("VietNhat")
                .setDirectoryName("Image")
                .setMultipleMode(false)
                .setSelectedImages(images)
                .setRequestCode(100)
                .start()
        }
        userId?.let { addGroupViewModel.getRecentFriend(it) }
        addGroupViewModel.listFriendResponse.observe(this, androidx.lifecycle.Observer { response ->
            gotFriendList(response.data)
        })
        RxSearch.fromView(editSearch)
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (TextUtils.isDigitsOnly(it)) {
                    filterByPhone(it)
                } else {
                    filterByName(it)
                }
            }
        tvAddGroup.setOnClickListener {
            if (isValidate()) {
                addGroupViewModel.postGroup(
                    images!!,
                    editGroupName.text.toString(),
                    addedFriendAdapter!!.addedFriendList,
                    userId!!,
                    groupID
                )
            }
        }

        addGroupViewModel.createGroupResponse.observe(this, androidx.lifecycle.Observer {
            findNavController().popBackStack()
        })
    }

    private fun isValidate(): Boolean {
        if (editGroupName.text.isNullOrBlank() && groupID.isNullOrBlank()) {
            Toast.makeText(context, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addedFriendAdapter!!.addedFriendList.isNullOrEmpty()) {
            Toast.makeText(
                context,
                "Vui lòng chọn ít nhất một người bạn",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun filterByName(nameRegex: String?) {
        nameRegex?.let { name ->
            var filterList = arrayListOf<FriendItem>()
            for (item in friendList) {
                if (item.name.toLowerCase().contains(name.toLowerCase())) {
                    filterList.add(item)
                }
            }
            friendAdapter!!.replaceData(filterList)
        }
    }

    private fun filterByPhone(phoneRegex: String?) {
        phoneRegex?.let { phone ->
            var filterList = arrayListOf<FriendItem>()
            for (item in friendList) {
                if (item.phone.contains(phone)) {
                    filterList.add(item)
                }
            }
            friendAdapter!!.replaceData(filterList)
        }
    }

    private fun gotFriendList(friendList: List<FriendItem>) {

        var wrapper = arrayListOf<FriendItem>()
        wrapper.addAll(friendList)

        if (!TextUtils.isEmpty(groupUserID)) {
            var hasFriendID = Gson().fromJson<ArrayList<String>>(groupUserID,
                object : TypeToken<ArrayList<String>>() {}.type)
            for (item in hasFriendID) {
                var iterator = wrapper.iterator()
                while (iterator.hasNext()) {
                    var friend = iterator.next()
                    if (friend.id == item) {
                        iterator.remove()
                    }
                }
            }
        }


        this.friendList = wrapper
        friendAdapter = FriendAdapter(wrapper)
        friendAdapter!!.setOnItemClickListener {
            addedFriendAdapter!!.addFriend(friendAdapter!!.friendItems[it])
            friendAdapter!!.onAddFriend(it)
        }
        rvListItem.adapter = friendAdapter
    }

    override fun initAction() {
    }

    override fun getBaseViewModel(): AddGroupViewModel {
        addGroupViewModel =
            ViewModelProviders.of(this, viewModelFactory)[AddGroupViewModel::class.java]
        return addGroupViewModel
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun initData(argument: Bundle?) {

    }

    override fun isShowActionBar(): View? = SimpleActionBar(activity).apply {
        simpleTitleText = "Tạo nhóm mới"
        leftButtonVisible = true
        rightButtonVisible = false
        leftActionBarButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun isActionBarOverlap(): Boolean = false

    override fun isShowBottomNavigation(): Boolean = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(EXTRA_IMAGES)
            // Do stuff with image's path or id. For example:
            images?.let {
                for ((id, _, path) in it) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val uri = Uri.withAppendedPath(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString()
                        )
                        Glide.with(context!!)
                            .load(uri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(icAddImage!!)
                    } else {
                        Glide.with(context!!)
                            .load(path)
                            .apply(RequestOptions.circleCropTransform())
                            .into(icAddImage!!)
                    }
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}