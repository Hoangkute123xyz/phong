package vn.hexagon.vietnhat.data.model.gift

import com.google.gson.annotations.SerializedName

data class JsonMember(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null
)