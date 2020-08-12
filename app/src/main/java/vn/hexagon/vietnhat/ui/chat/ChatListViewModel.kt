package vn.hexagon.vietnhat.ui.chat

import androidx.lifecycle.MutableLiveData
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.chat.CommunityContentResponse
import vn.hexagon.vietnhat.data.model.chat.GroupListResponse
import vn.hexagon.vietnhat.data.model.remote.ChatList
import vn.hexagon.vietnhat.repository.chat.ChatListRepository
import javax.inject.Inject

/**
 * Created by NhamVD on 2019-09-22.
 */
class ChatListViewModel @Inject constructor(
    private val repo: ChatListRepository
) : MVVMBaseViewModel() {
    var chatData = MutableLiveData<List<ChatList>>()
    var communityData = MutableLiveData<CommunityContentResponse>()
    var groupListResponse = MutableLiveData<GroupListResponse>()

    fun getChatList(userId: String) {
        repo.getListChat(userId = userId)
            .applyScheduler()
            .subscribe({ response ->
                chatData.postValue(response.data)
            }, {

            })
            .addToCompositeDisposable(compositeDisposable)
    }

    fun getCommunityContent(userId: String) {
        repo.getCommunityContent(userId)
            .applyScheduler()
            .subscribe(
                { response ->
                    communityData.postValue(response)
                }, {

                }
            )
            .addToCompositeDisposable(compositeDisposable)
    }

    fun getGroupChatList(userId: String) {
        repo.getGroupChat(userId).applyScheduler().subscribe({ response ->
            groupListResponse.postValue(response)
        }, { throwable -> throwable.printStackTrace() })
            .addToCompositeDisposable(compositeDisposable)
    }
}