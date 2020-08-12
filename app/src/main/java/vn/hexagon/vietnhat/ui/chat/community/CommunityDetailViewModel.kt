package vn.hexagon.vietnhat.ui.chat.community

import androidx.lifecycle.MutableLiveData
import com.nguyenhoanglam.imagepicker.model.Image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.*
import vn.hexagon.vietnhat.constant.APIConstant
import vn.hexagon.vietnhat.data.model.chat.CommunityContent
import vn.hexagon.vietnhat.data.model.chat.CommunityContentResponse
import vn.hexagon.vietnhat.data.model.chat.GroupListResponse
import vn.hexagon.vietnhat.data.model.remote.SocketResponse
import vn.hexagon.vietnhat.data.remote.SocketService
import vn.hexagon.vietnhat.repository.chat.ChatRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CommunityDetailViewModel @Inject constructor(
    private val repo: ChatRepository,
    private val socketIO: SocketService
) : MVVMBaseViewModel() {

    var communityContentResponse = MutableLiveData<Pair<Boolean, CommunityContentResponse>>()
    var communityContent: CommunityContentResponse? = null
    var groupListResponse = MutableLiveData<GroupListResponse>()

    var userAvatar:String?=null
    var userName:String?=null


    fun getGroupChatList(userId: String) {
        repo.getGroupChat(userId).applyScheduler().subscribe({ response ->
            groupListResponse.postValue(response)
        }, { throwable -> throwable.printStackTrace() })
            .addToCompositeDisposable(compositeDisposable)
    }
    fun getContent(userId: String) {

        repo.getCommunityContentChat(userId)
            .applyScheduler()
            .subscribe(
                { response ->
                    communityContent = response
                    communityContentResponse.postValue(
                        Pair(
                            false,
                            response
                        ) as Pair<Boolean, CommunityContentResponse>?
                    )
                }, { throwable ->
                    DebugLog.e(throwable.toString())
                }
            )
            .addToCompositeDisposable(compositeDisposable)
    }

    fun getGroupContent(userId: String, groupID: String) {
        repo.getGroupChatContent(userId, groupID).applyScheduler().subscribe({ response ->
            communityContent = response
            communityContentResponse.postValue(
                Pair(
                    false,
                    response
                ) as Pair<Boolean, CommunityContentResponse>?
            )
        }, { throwable ->
            DebugLog.e(throwable.toString())
        })
            .addToCompositeDisposable(compositeDisposable)
    }

    fun sendMessage(
        userId: String,
        chatPlaceID: String,
        content: String,
        type: String,
        images: ArrayList<Image>?
    ) {
        var requestFile: MultipartBody.Part? = null
        images?.let {
            val img = it[0]
            if (!img.path.isBlank()) {
                val file = File(img.path)
                // Upload image file
                val requestImg = RequestBody.create(MediaType.parse("*/*"), file)
                requestFile = MultipartBody.Part.createFormData("img", file.name, requestImg)

            }
        }

        val body = MultipartBody.Builder()
        with(body) {
            setType(MultipartBody.FORM)
            requestFile?.let { file ->
                addPart(file)
            }
            addFormDataPart("user_id", userId)
            addFormDataPart("group_id", chatPlaceID)
            addFormDataPart("type", type)
            addFormDataPart("content", content)
        }
        repo.postSendMessageGroup(body.build()).applyScheduler().subscribe(
            { response ->
                val contentSocket = CommunityContent(
                    getCurrentDateTime(),
                    userAvatar,
                    userId,
                    userName,
                    "",
                    chatPlaceID,
                    type,
                    response.data
                )
                communityContent?.data?.add(0,contentSocket)
                communityContent?.let {
                        content->communityContentResponse.postValue(Pair(true,content))
                }

                val socketData =
                    SocketResponse(APIConstant.SOCKET_EVENT_RECEIVE_MESSAGE_GROUP, contentSocket)
                socketIO.request(
                    APIConstant.SOCKET_EVENT_RECEIVE_MESSAGE_GROUP,
                    Utils.toJson(socketData)
                )
                    .applyScheduler()
                    .subscribe({},
                        { socketError ->
                            DebugLog.e(socketError.toString())
                        })
                    .addToCompositeDisposable(compositeDisposable)
            }, { error ->
                DebugLog.e(error.toString())
            }
        ).addToCompositeDisposable(compositeDisposable)

    }

    fun sendMessage(
        userId: String,
        type: String,
        content: String,
        images: ArrayList<Image>?
    ) {
        var requestFile: MultipartBody.Part? = null
        images?.let {
            val img = it[0]
            if (!img.path.isBlank()) {
                val file = File(img.path)
                // Upload image file
                val requestImg = RequestBody.create(MediaType.parse("*/*"), file)
                requestFile = MultipartBody.Part.createFormData("img", file.name, requestImg)

            }
        }

        val body = MultipartBody.Builder()
        with(body) {
            setType(MultipartBody.FORM)
            requestFile?.let { file ->
                addPart(file)
            }
            addFormDataPart("user_id", userId)
            addFormDataPart("type", type)
            addFormDataPart("content", content)
        }
        repo.postMessageCommunityChat(body.build()).applyScheduler().subscribe(
            { response ->
                val contentSocket: CommunityContent = CommunityContent(
                    getCurrentDateTime(),
                    userAvatar,
                    userId,
                    userName,
                    "",
                    "",
                    type,
                    response.data
                )

                communityContent?.data?.add(0,contentSocket)
                communityContent?.let {
                    content->communityContentResponse.postValue(Pair(true,content))
                }


                val socketData =
                    SocketResponse(APIConstant.SOCKET_EVENT_RECEIVE_MESSAGE_COMM, contentSocket)
                socketIO.request(
                    APIConstant.SOCKET_EVENT_RECEIVE_MESSAGE_COMM,
                    Utils.toJson(socketData)
                )
                    .applyScheduler()
                    .subscribe({},
                        { socketError ->
                            DebugLog.e(socketError.toString())
                        })
                    .addToCompositeDisposable(compositeDisposable)
            }, { error ->
                DebugLog.e(error.toString())
            }
        ).addToCompositeDisposable(compositeDisposable)

    }

    fun subIncomingMessageGroup(
        userId: String,
        groupID: String
    ) {
        socketIO.handleResponse(APIConstant.SOCKET_EVENT_SEND_MESSAGE_GROUP)
            .toMainThread()
            .subscribe { response ->
                val socketResponse = Utils.fromJson<SocketResponse>(response)
                when (socketResponse.event) {
                    APIConstant.SOCKET_EVENT_RECEIVE_MESSAGE_GROUP -> {
                        val data: CommunityContent =
                            Utils.fromJson(JSONObject(socketResponse.data as MutableMap<*, *>).toString())
                       if (data.id.equals(groupID)){
                           communityContent!!.data.add(0,data)
                           communityContentResponse.postValue(
                               Pair(
                                   false,
                                   communityContent
                               ) as Pair<Boolean, CommunityContentResponse>
                           )
                       }
                    }
                }
            }
            .addToCompositeDisposable(compositeDisposable)
    }

    fun subIncomingMessageComm(userId: String) {
        socketIO.handleResponse(APIConstant.SOCKET_EVENT_SEND_MESSAGE_COMM)
            .toMainThread()
            .subscribe { response ->
                val socketResponse = Utils.fromJson<SocketResponse>(response)
                when (socketResponse.event) {
                    APIConstant.SOCKET_EVENT_RECEIVE_MESSAGE_COMM -> {
                        val data: CommunityContent =
                            Utils.fromJson(JSONObject(socketResponse.data as MutableMap<*, *>).toString())
                        communityContent!!.data.add(0,data)
                        communityContentResponse.postValue(
                            Pair(
                                false,
                                communityContent
                            ) as Pair<Boolean, CommunityContentResponse>
                        )
                    }
                }
            }
            .addToCompositeDisposable(compositeDisposable)
    }

    fun getCurrentDateTime(): String {
        var output = ""
        val outFmt =
            SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US)
        var current = Calendar.getInstance().time
        try {
            output = outFmt.format(current)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return output

    }

}