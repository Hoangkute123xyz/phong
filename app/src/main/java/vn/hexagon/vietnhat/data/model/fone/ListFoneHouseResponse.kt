package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class ListFoneHouseResponse(
    @SerializedName("data")
    var data: ArrayList<Fone>,
    @SerializedName("errorId")
    val errorId: String,
    @SerializedName("message")
    val message: String
)