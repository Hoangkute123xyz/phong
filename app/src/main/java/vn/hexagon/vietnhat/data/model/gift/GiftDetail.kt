package vn.hexagon.vietnhat.data.model.gift

import com.google.gson.annotations.SerializedName
import vn.hexagon.vietnhat.data.model.product.Product

data class GiftDetail(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("img")
    val img: List<String>? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("lng")
    val lng: String? = null,

    @field:SerializedName("active")
    val active: String? = null,

    @field:SerializedName("is_favourite")
    var isFavourite: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("number_comment")
    val numberComment: Int? = null,

    @field:SerializedName("number_favourite")
    val numberFavourite: Int? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @SerializedName("list_product")
    val products: List<Product>,
    @SerializedName("code")
    val code: String
)