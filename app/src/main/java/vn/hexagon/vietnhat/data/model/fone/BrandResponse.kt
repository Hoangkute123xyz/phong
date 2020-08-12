package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class BrandResponse(
    @SerializedName("data")
    val data: ArrayList<Brand>,

    @SerializedName("errorId")
    val errorId: Int,

    @SerializedName("message")
    val message: String
)