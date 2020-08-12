package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class Brand(

    @SerializedName("date")
    val date: String,

    @SerializedName("img")
    val img: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("active")
    val active: String,

    @SerializedName("id")
    val id: String
) {
     var listProduct: List<ProductModel>? = null

}