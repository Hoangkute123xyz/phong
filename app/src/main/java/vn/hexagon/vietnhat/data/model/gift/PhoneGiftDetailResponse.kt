package vn.hexagon.vietnhat.data.model.gift

import com.google.gson.annotations.SerializedName

data class PhoneGiftDetailResponse(

    @field:SerializedName("data")
	val giftDetail: GiftDetail? = null,

    @field:SerializedName("errorId")
	val errorId: Int? = null,

    @field:SerializedName("message")
	val message: String? = null
)