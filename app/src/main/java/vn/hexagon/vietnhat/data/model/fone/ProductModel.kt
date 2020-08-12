package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.annotations.SerializedName

data class ProductModel(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("brand_id")
	val brandId: String? = null
)