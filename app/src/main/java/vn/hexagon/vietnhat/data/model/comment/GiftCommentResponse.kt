package vn.hexagon.vietnhat.data.model.comment

import com.google.gson.annotations.SerializedName

data class GiftCommentResponse(

    @field:SerializedName("data")
	val data: List<GiftCommentItem> ,

    @field:SerializedName("errorId")
	val errorId: Int,

    @field:SerializedName("message")
	val message: String
)