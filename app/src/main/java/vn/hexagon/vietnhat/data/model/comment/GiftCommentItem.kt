package vn.hexagon.vietnhat.data.model.comment

import com.google.gson.annotations.SerializedName

data class GiftCommentItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("comment")
	val comment: String,

	@field:SerializedName("avatar")
	val avatar: String
)