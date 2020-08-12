package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class PhoneGiftResponse(

	@field:SerializedName("data")
	val data: List<PhoneGift?>? = null,

	@field:SerializedName("errorId")
	val errorId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)