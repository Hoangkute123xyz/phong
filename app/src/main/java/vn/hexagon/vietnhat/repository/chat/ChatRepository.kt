package vn.hexagon.vietnhat.repository.chat

import io.reactivex.Single
import okhttp3.MultipartBody
import vn.hexagon.vietnhat.data.model.chat.CommunityChatResponse
import vn.hexagon.vietnhat.data.model.chat.CommunityContentResponse
import vn.hexagon.vietnhat.data.model.chat.CreateGroupResponse
import vn.hexagon.vietnhat.data.model.chat.GroupListResponse
import vn.hexagon.vietnhat.data.model.remote.ChatDetailResponse
import vn.hexagon.vietnhat.data.model.remote.ChatResponse
import vn.hexagon.vietnhat.data.remote.NetworkService
import javax.inject.Inject

/**
 * Created by NhamVD on 2019-09-29.
 */
class ChatRepository @Inject constructor(
    private val service: NetworkService
) {
    fun getContentChat(userId: String, userChatId: String): Single<ChatDetailResponse> {
        return service.getContentChat(userId = userId, userChatId = userChatId)
    }

    fun sendMessageChat(
        userId: String,
        userReceiveId: String,
        content: String
    ): Single<ChatResponse> {
        return service.sendMessageChat(
            userIdSend = userId,
            userIdReceive = userReceiveId,
            content = content
        )
    }

    fun getCommunityContentChat(userID: String): Single<CommunityContentResponse> {
        return service.getCommunityContent(userID)
    }

    fun getGroupChatContent(userID: String, groupID: String): Single<CommunityContentResponse> {
        return service.getGroupChatContent(userID, groupID)
    }
    fun postSendMessageGroup(body: MultipartBody): Single<CommunityChatResponse> {
        return service.postMessageGroupChat(body)
    }

    fun postMessageCommunityChat(body: MultipartBody): Single<CommunityChatResponse>  {
        return service.postMessageCommunityChat(body)
    }
    fun getGroupChat(userId: String):Single<GroupListResponse> {
        return service.getChatGroupList(userId)
    }
}