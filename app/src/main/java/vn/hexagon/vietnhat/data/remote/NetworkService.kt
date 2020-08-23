package vn.hexagon.vietnhat.data.remote

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import vn.hexagon.vietnhat.data.model.ExampleUser
import vn.hexagon.vietnhat.data.model.PostDetailResponse
import vn.hexagon.vietnhat.data.model.auth.*
import vn.hexagon.vietnhat.data.model.banner.BannerResponse
import vn.hexagon.vietnhat.data.model.chat.*
import vn.hexagon.vietnhat.data.model.comment.CommentResponse
import vn.hexagon.vietnhat.data.model.comment.ListFavoritePhoneResponse
import vn.hexagon.vietnhat.data.model.fone.*
import vn.hexagon.vietnhat.data.model.gift.PhoneGiftDetailResponse
import vn.hexagon.vietnhat.data.model.guide.GuideDetailResponse
import vn.hexagon.vietnhat.data.model.guide.GuideResponse
import vn.hexagon.vietnhat.data.model.job.JobResponse
import vn.hexagon.vietnhat.data.model.mart.MartDetail
import vn.hexagon.vietnhat.data.model.mart.MartResponse
import vn.hexagon.vietnhat.data.model.news.NewsDetailResponse
import vn.hexagon.vietnhat.data.model.news.NewsListResponse
import vn.hexagon.vietnhat.data.model.news.SubjectResponse
import vn.hexagon.vietnhat.data.model.notification.NotifyResponse
import vn.hexagon.vietnhat.data.model.product.ProductResponse
import vn.hexagon.vietnhat.data.model.rate.RateResponse
import vn.hexagon.vietnhat.data.model.remote.ChatDetailResponse
import vn.hexagon.vietnhat.data.model.remote.ChatListResponse
import vn.hexagon.vietnhat.data.model.remote.ChatResponse
import vn.hexagon.vietnhat.data.model.service.ListPostResponse
import vn.hexagon.vietnhat.data.model.translator.PostResponse
import vn.hexagon.vietnhat.data.model.translator.TranslatorListResponse
import vn.hexagon.vietnhat.data.model.warrant.WarrantResponse
import java.util.*

/**
 * Created by NhamVD on 2019-07-29.
 */
interface NetworkService {
    @GET("/users/{user}")
    fun getUser(@Path("user") userId: String): Single<ExampleUser>

    @POST("/register.php")
    fun registerUser(@Body user: User): Single<User>

    @FormUrlEncoded
    @POST("login.php")
    fun doLogin(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Single<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun doSignUp(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Single<LoginResponse>

    @FormUrlEncoded
    @POST("loginfb.php")
    fun sendFbInfo(
        @Field("email") email: String,
        @Field("fbid") fbid: String,
        @Field("name") name: String
    ): Single<LoginResponse>

    @FormUrlEncoded
    @POST("logingg.php")
    fun sendGgInfo(
        @Field("email") email: String,
        @Field("ggid") ggid: String,
        @Field("name") name: String
    ): Single<LoginResponse>

    @GET("get_info.php")
    fun getUserInfo(@Query("user_id") userId: String): Single<LoginResponse>

    @FormUrlEncoded
    @POST("update_profile.php")
    fun requestUpdateProfile(
        @Field("user_id") userId: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("name") name: String,
        @Field("address") address: String
    ): Single<LoginResponse>

    @Multipart
    @POST("update_avatar.php")
    fun uploadAvatar(
        @Part("user_id") userId: RequestBody,
        @Part img: MultipartBody.Part?
    ): Single<LoginResponse>


    @POST("create_group.php")
    fun postGroup(
        @Body body: MultipartBody
    ): Single<CreateGroupResponse>

    @FormUrlEncoded
    @POST("change_password.php")
    fun requestUpdatePassword(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Single<PasswordResponse>

    @GET("movie/popular")
    fun getMart(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Single<MartResponse>

    @GET("movie/{movie_id}")
    fun getMartDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<MartDetail>

    @Multipart
    @POST("post.php")
    fun createNormalPost(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("list_image") coverImages: List<@JvmSuppressWildcards RequestBody>,
        @Part coverImg: ArrayList<MultipartBody.Part?>
    ): Single<PostResponse>

    @GET("list_post.php")
    fun getListPost(
        @Query("user_id") userId: String,
        @Query("service_id") serviceId: String,
        @Query("index") index: Int,
        @Query("number_post") number: Int
    ): Single<TranslatorListResponse>

    @GET("list_post.php")
    fun getPostList(
        @Query("user_id") userId: String,
        @Query("service_id") serviceId: String,
        @Query("index") index: Int,
        @Query("number_post") number: Int,
        @Query("lat") lat: String?,
        @Query("lng") lng: String?
    ): Single<ListPostResponse>


    @GET("list_phone_house.php")
    fun getPhoneHouse(
        @Query("index") index: Int,
        @Query("number_post") number: Int
    ): Single<ListFoneHouseResponse>

    @GET("list_favourite.php")
    fun getFavList(
        @Query("user_id") userId: String,
        @Query("service_id") serviceId: String?
    ): Single<ListPostResponse>

    @GET("job_type.php")
    fun getJobType(): Single<JobResponse>

    @FormUrlEncoded
    @POST("favourite.php")
    fun getFavourite(
        @Field("user_id") userId: String,
        @Field("post_id") id: String
    ): Single<ListPostResponse>

    @FormUrlEncoded
    @POST("favourite_gift.php")
    fun getFavouriteGift(
        @Field("user_id") userId: String,
        @Field("gift_id") giftID: String
    ): Single<ListPostResponse>

    @FormUrlEncoded
    @POST("un_favourite.php")
    fun removeFavourite(
        @Field("user_id") userId: String,
        @Field("post_id") id: String
    ): Single<ListPostResponse>

    @FormUrlEncoded
    @POST("un_favourite_gift.php")
    fun removeFavouriteGift(
        @Field("user_id") userId: String,
        @Field("gift_id") giftID: String
    ): Single<ListPostResponse>

    @Multipart
    @POST("edit_post.php")
    fun requestEditPost(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("list_image") coverImages: List<@JvmSuppressWildcards RequestBody>,
        @Part coverImg: ArrayList<MultipartBody.Part?>
    ): Single<PostResponse>

    @GET("post_detail.php")
    fun getDetailPost(
        @Query("user_id") userId: String,
        @Query("post_id") postId: String?
    ): Single<PostDetailResponse>

    @GET("list_product.php")
    fun getProductList(@Query("post_id") postId: String): Single<ProductResponse>

    @GET("instruction.php")
    fun getGuide(): Single<GuideResponse>

    @GET("instruction_detail.php")
    fun getGuideDetail(@Query("instruction_id") id: String): Single<GuideDetailResponse>

    @GET("banner.php")
    fun getBanner(): Single<BannerResponse>

    @FormUrlEncoded
    @POST("rating.php")
    fun requestRating(
        @Field("user_id") userId: String,
        @Field("star") star: String,
        @Field("content") content: String?
    ): Single<RateResponse>

    @Multipart
    @POST("post.php")
    fun createPostWithProduct(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("list_product") bodies: List<@JvmSuppressWildcards RequestBody>,
        @Part img: List<MultipartBody.Part?>,
        @Part("list_image") coverImages: List<@JvmSuppressWildcards RequestBody>,
        @Part coverImg: List<MultipartBody.Part?>
    ): Single<PostResponse>

    @Multipart
    @POST("edit_post.php")
    fun editPostProduct(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("list_product") bodies: List<@JvmSuppressWildcards RequestBody>,
        @Part img: ArrayList<MultipartBody.Part?>,
        @Part("list_image") coverImages: List<@JvmSuppressWildcards RequestBody>,
        @Part coverImg: ArrayList<MultipartBody.Part?>
    ): Single<PostResponse>

    @GET("search_support.php")
    fun getCommonSearchResult(
        @Query("user_id") userId: String,
        @Query("service_id") serviceId: String,
        @Query("title") title: String?,
        @Query("address") address: String?,
        @Query("index") index: Int,
        @Query("number_post") number_post: Int
    ): Single<ListPostResponse>

    @GET("search_news.php")
    fun getNewSearchResult(
        @Query("user_id") userId: String,
        @Query("title") title: String?,
        @Query("subject_id") subjectId: String?,
        @Query("index") index: Int,
        @Query("number_post") number_post: Int
    ): Single<ListPostResponse>

    @GET("search_transport.php")
    fun getSearchDeliver(
        @Query("user_id") userId: String,
        @Query("title") title: String?,
        @Query("road_type") roadType: String?,
        @Query("index") index: Int,
        @Query("number_post") number_post: Int
    ): Single<ListPostResponse>

    @GET("list_notification.php")
    fun getListNotifications(@Query("user_id") userId: String): Single<NotifyResponse>

    @GET("manage_post.php")
    fun getPersonalPost(
        @Query("user_id") userId: String,
        @Query("service_id") serviceId: String?,
        @Query("index") index: Int,
        @Query("number_post") number: Int
    ): Single<ListPostResponse>

    @GET("list_chat.php")
    fun getListChat(@Query("user_id") userId: String): Single<ChatListResponse>

    @GET("community_content.php")
    fun getCommunityContent(@Query("user_id") userID: String): Single<CommunityContentResponse>

    @GET("content_group_chat.php")
    fun getGroupChatContent(
        @Query("user_id") userID: String,
        @Query("group_id") groupID: String
    ): Single<CommunityContentResponse>

    @GET("content_chat.php")
    fun getContentChat(
        @Query("user_id") userId: String,
        @Query("user_chat_id") userChatId: String
    ): Single<ChatDetailResponse>

    @FormUrlEncoded
    @POST("chat.php")
    fun sendMessageChat(
        @Field("user_id_send") userIdSend: String,
        @Field("user_id_receive") userIdReceive: String,
        @Field("content") content: String
    ): Single<ChatResponse>

    @FormUrlEncoded
    @POST("delete_post.php")
    fun removePost(
        @Field("user_id") userId: String,
        @Field("post_id") id: String
    ): Single<ListPostResponse>

    @GET("news_category.php")
    fun getSubject(): Single<SubjectResponse>

    @GET("verify_code.php")
    fun getCodeVerify(@Query("phone") phone: String): Single<VerifyResponse>

    @FormUrlEncoded
    @POST("forget_password.php")
    fun requestForgotPassword(@Field("phone") phone: String): Single<VerifyResponse>

    @FormUrlEncoded
    @POST("update_token_fcm.php")
    fun requestUpdateToken(
        @Field("user_id") userId: String,
        @Field("token_fcm") token: String
    ): Single<FcmTokenResponse>

    @GET("list_news.php")
    fun getNewsList(
        @Query("index") index: Int,
        @Query("number") number: Int
    ): Single<NewsListResponse>

    @GET("list_paper.php")
    fun getSupportList(
        @Query("index") index: Int,
        @Query("number") number: Int
    ): Single<NewsListResponse>

    @GET("news_detail.php")
    fun getNewsDetail(@Query("news_id") newsId: String): Single<NewsDetailResponse>

    @FormUrlEncoded
    @POST("comment.php")
    fun requestComment(
        @Field("user_id") userId: String,
        @Field("post_id") postId: String,
        @Field("content") content: String
    ): Single<CommentResponse>

    @GET("phone_house_detail.php")
    fun requestFoneHouseDetail(
        @Query("user_id") userId: String,
        @Query("product_id") productID: String
    ): Single<FoneDetailResponse>

    @GET("brand.php")
    fun getBrand(
        @Query("user_id") userId: String
    ): Single<BrandResponse>

    @GET("product_type.php")
    fun getModelProduct(
        @Query("brand_id") brandID: String?
    ): Single<ProductModelResponse>

    @GET("search_phone_house.php")
    fun searchPhoneHouse(
        @Query("brand_id") brandID: String?,
        @Query("type_id") modelID: String?,
        @Query("index") index: Int,
        @Query("number_post") number: Int
    ): Single<ListFoneHouseResponse>

    @GET("list_gift.php")
    fun getPhoneGift(
        @Query("index") index: Int,
        @Query("number_post") numberPost: Int
    ): Single<PhoneGiftResponse>

    @GET("gift_detail.php")
    fun getPhoneGiftDetail(
        @Query("user_id") userID: String,
        @Query("gift_id") giftID: String
    ): Single<PhoneGiftDetailResponse>

    @FormUrlEncoded
    @POST("favourite_gift.php")
    fun postPhoneGiftFavourite(
        @Field("user_id") userID: String,
        @Field("gift_id") giftID: String
    ): Single<ListPostResponse>

    @FormUrlEncoded
    @POST("un_favourite_gift.php")
    fun postPhoneGiftUnFavourite(
        @Field("user_id") userID: String,
        @Field("gift_id") giftID: String
    ): Single<ListPostResponse>

    @GET("list_comment_gift.php")
    fun getGiftComment(
        @Query("gift_id") giftID: String
    )

    @FormUrlEncoded
    @POST("comment_phone.php")
    fun postCommentPhone(
        @Field("user_id") userID: String,
        @Field("id") id: String,
        @Field("comment") comment: String,
        @Field("type") type: Int

    ): Single<CommentResponse>

    @GET("time.php")
    fun getTimeRange(): Single<TimeRangeResponse>

    @FormUrlEncoded
    @POST("booking.php")
    fun postBooking(
        @Field("user_id") userID: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("product_code") productCode: String,
        @Field("address") address: String,
        @Field("email") email: String,
        @Field("price") price: String,
        @Field("time_id") timeID: String
    ): Single<TimeRangeResponse>

    @GET("search_phone_house.php")
    fun getPhoneHouseSearch(
        @Query("brand_id") brandID: String,
        @Query("type_id") typeID: String,
        @Query("index") index: Int,
        @Query("number_post") number: Int
    ): Single<ListFoneHouseResponse>

    @GET("list_user.php")
    fun getFriendChatList(
        @Query("user_id") userID: String
    ): Single<ListFriendResponse>

    @GET("list_favourite_phone.php")
    fun getListFavoritePhone(@Query("product_id") productID: String): Single<ListFavoritePhoneResponse>


    @GET("list_group_chat.php")
    fun getChatGroupList(
        @Query("user_id") userID: String
    ): Single<GroupListResponse>


    @POST("chat_group.php")
    fun postMessageGroupChat(
        @Body body: MultipartBody
    ): Single<CommunityChatResponse>


    @POST("chat_community.php")
    fun postMessageCommunityChat(
        @Body body: MultipartBody
    ): Single<CommunityChatResponse>

    @GET("warrant.php")
    fun getWarrant(
        @Query("user_id") userID: String,
        @Query("phone") phone: String
    ): Single<WarrantResponse>

    @FormUrlEncoded
    @POST("add_user.php")
    fun postAddFriend(
        @Field("user_id") userID: String,
        @Field("group_id") groupID: String
    ): Single<CreateGroupResponse>

    @GET("list_comment.php")
    fun getCommentFoneHouse(@Query("product_id") productId:String):Single<FoneComment>
}