package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class FoneDetailResponse(
    @SerializedName("data")
    val data: Fone,
    @SerializedName("errorId")
    val errorId: Int,
    @SerializedName("message")
    val message: String
)