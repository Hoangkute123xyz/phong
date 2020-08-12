package vn.hexagon.vietnhat.data.model.gift

import com.google.gson.annotations.SerializedName

data class CommentResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("errorId")
	val errorId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)