package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class Fone(

    @SerializedName("date")
    val date: String,

    @SerializedName("price_discount")
    val priceDiscount: String,

    @SerializedName("img")
    val img: List<String>,

    @SerializedName("code")
    val code: String,

    @SerializedName("memory")
    val memory: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("lng")
    val lng: String,

    @SerializedName("type_id")
    val typeId: String,

    @SerializedName("active")
    val active: String,

    @SerializedName("brand_id")
    val brandId: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("instruction")
    val instruction: String,

    @SerializedName("contact")
    val contact: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("warrant")
    val warrant: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("descript")
    val descript: String,

    @SerializedName("lat")
    val lat: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("is_favourite")
    val favourite: Int,

    @SerializedName("number_comment")
    val numberComment: String? = "0",

    @SerializedName("number_favourite")
    val numberFavourite: String? = "0"

)