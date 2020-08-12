package vn.hexagon.vietnhat.ui.chat.add

import androidx.lifecycle.MutableLiveData
import com.nguyenhoanglam.imagepicker.model.Image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.chat.CreateGroupResponse
import vn.hexagon.vietnhat.data.model.chat.FriendItem
import vn.hexagon.vietnhat.data.model.chat.ListFriendResponse
import vn.hexagon.vietnhat.repository.detail.FoneHouseDetailRepository
import java.io.File
import java.util.*
import javax.inject.Inject

class AddGroupViewModel @Inject constructor(private val repository: FoneHouseDetailRepository) :
    MVVMBaseViewModel() {
    var listFriendResponse = MutableLiveData<ListFriendResponse>()
    var createGroupResponse = MutableLiveData<CreateGroupResponse>()

    fun getRecentFriend(userID: String) {
        repository.getRecentFriend(userID)
            .applyScheduler()
            .subscribe({ response ->
                listFriendResponse.postValue(response)
            }, { throwable ->
                throwable.printStackTrace()
            }
            ).addToCompositeDisposable(compositeDisposable)
    }

    fun postGroup(
        images: ArrayList<Image>,
        groupName: String,
        addedFriendList: ArrayList<FriendItem>,
        userId: String,
        groupID: String?
    ) {
        if (groupID.isNullOrBlank()) {
            var requestFile: MultipartBody.Part? = null
            if (!images.isNullOrEmpty()) {
                var img = images[0]
                if (!img.path.isBlank()) {
                    val file = File(img.path)
                    // Upload image file
                    val requestImg = RequestBody.create(MediaType.parse("*/*"), file)
                    requestFile = MultipartBody.Part.createFormData("img", file.name, requestImg)

                }
            }
            var groupMember = userId
            for (i in addedFriendList.indices) {
                groupMember = "$groupMember,"
                groupMember += addedFriendList[i].id
            }
            var body = MultipartBody.Builder()
            with(body) {
                setType(MultipartBody.FORM)
                requestFile?.let { file ->
                    addPart(file)
                }
                addFormDataPart("user_id", userId)
                addFormDataPart("name", groupName)
                addFormDataPart("list_user_id", groupMember)
            }

            repository.postGroup(body.build())
                .applyScheduler()
                .subscribe(
                    { response ->
                        createGroupResponse.postValue(response)
                    }, { throwable ->
                        throwable.printStackTrace()
                    }
                )
                .addToCompositeDisposable(compositeDisposable)
        } else {
            var queue: PriorityQueue<String> = PriorityQueue()
            queue.add(userId)
            for (i in addedFriendList.indices) {
                queue.add(addedFriendList[i].id)
            }
            postAddFriend(queue, groupID)
        }
    }

    fun postAddFriend(userIDs: PriorityQueue<String>, groupID: String) {
       if (userIDs.isNotEmpty()){
           var userID = userIDs.poll()
           repository.postAddUser(userID, groupID)
               .applyScheduler()
               .subscribe(
                   {
                       postAddFriend(userIDs, groupID)
                   }, {
                       postAddFriend(userIDs, groupID)
                   }
               )
               .addToCompositeDisposable(compositeDisposable)
       }else{
           createGroupResponse.postValue(null)
       }
    }
}