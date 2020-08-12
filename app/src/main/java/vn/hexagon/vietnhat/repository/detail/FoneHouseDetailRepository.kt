package vn.hexagon.vietnhat.repository.detail

import io.reactivex.Single
import okhttp3.MultipartBody
import vn.hexagon.vietnhat.data.model.chat.CreateGroupResponse
import vn.hexagon.vietnhat.data.model.chat.ListFriendResponse
import vn.hexagon.vietnhat.data.model.comment.CommentResponse
import vn.hexagon.vietnhat.data.model.comment.ListFavoritePhoneResponse
import vn.hexagon.vietnhat.data.model.fone.BrandResponse
import vn.hexagon.vietnhat.data.model.fone.FoneDetailResponse
import vn.hexagon.vietnhat.data.model.fone.ProductModelResponse
import vn.hexagon.vietnhat.data.model.fone.TimeRangeResponse
import vn.hexagon.vietnhat.data.model.gift.PhoneGiftDetailResponse
import vn.hexagon.vietnhat.data.model.service.ListPostResponse
import vn.hexagon.vietnhat.data.remote.NetworkService
import java.util.*
import javax.inject.Inject

class FoneHouseDetailRepository @Inject constructor(private val apiService: NetworkService) {

    /**
     * Request phone detail
     */
    fun getDetailPost(userId: String, postId: String): Single<FoneDetailResponse> {
        return apiService.requestFoneHouseDetail(userId, postId)
    }

    /*
    * Get Brand list
    */
    fun getBrand(userId: String): Single<BrandResponse> {
        return apiService.getBrand(userId)
    }

    /*
    * Get Model Product
    */
    fun getModel(productID: String?): Single<ProductModelResponse> {
        return apiService.getModelProduct(productID)
    }

    // * Get phone gift detail
    // * @param userID
    // * @param giftID
    // * @return giftDetail
    fun getPhoneGiftDetail(userID: String, giftID: String): Single<PhoneGiftDetailResponse> {
        return apiService.getPhoneGiftDetail(userID, giftID)
    }


    // * Favourite Gift
    // * @param userID
    // * @param giftID
    fun favouriteGift(userID: String, giftID: String): Single<ListPostResponse> {
        return apiService.postPhoneGiftFavourite(userID, giftID)
    }


    // * Un Favourite Gift
    // * @param userID
    // * @param giftID
    fun unFavouriteGift(userID: String, giftID: String): Single<ListPostResponse> {
        return apiService.postPhoneGiftUnFavourite(userID, giftID)
    }

    // * Comment a phone or a gift
    // * @param id
    // * @param user_id
    // * @param type : type = 1: Phone house; 2: Phone Gift
    // * @param comment

    fun commentPhone(
        id: String,
        user_id: String,
        type: Int,
        comment: String
    ): Single<CommentResponse> {
        return apiService.postCommentPhone(user_id, id, comment, type)
    }


    /*Get Time Range*/
    fun getTimeRange(): Single<TimeRangeResponse> {
        return apiService.getTimeRange()
    }

    /*Post booking*/
    fun postBooking(
        userID: String,
        userName: String,
        phone: String,
        address: String,
        email: String,
        productCode: String,
        productPrice: String,
        timeRangeID: String
    ): Single<TimeRangeResponse> {
        return apiService.postBooking(
            userID,
            userName,
            phone,
            productCode,
            address,
            email,
            productPrice,
            timeRangeID
        )
    }

    fun getRecentFriend(userID: String): Single<ListFriendResponse> {
        return apiService.getFriendChatList(userID)
    }

    fun postGroup(body: MultipartBody):Single<CreateGroupResponse>{
        return apiService.postGroup(body)
    }

    fun getListFavoritePhone(productID:String):Single<ListFavoritePhoneResponse>{
        return apiService.getListFavoritePhone(productID)
    }

    fun postAddUser(userID :String,groupID:String):Single<CreateGroupResponse>{
        return apiService.postAddFriend(userID, groupID)
    }

}