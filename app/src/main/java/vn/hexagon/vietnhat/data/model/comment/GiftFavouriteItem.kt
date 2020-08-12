package vn.hexagon.vietnhat.data.model.comment

import com.google.gson.annotations.SerializedName

data class GiftFavouriteItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null
)