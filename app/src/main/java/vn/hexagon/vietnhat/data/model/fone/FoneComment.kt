package vn.hexagon.vietnhat.data.model.fone

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class FoneComment (
    @SerializedName("data") val data: ArrayList<DataField>,
    @SerializedName("errorId") val errorId: Int,
    @SerializedName("message") val message: String
) {
    companion object {
        fun create(json: String): FoneComment {
            val gson = GsonBuilder().create()
            return gson.fromJson(json,  FoneComment::class.java)
        }
    }

    override fun toString(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this)
    }


    data class DataField (
        @SerializedName("name") val name: String,
        @SerializedName("avatar") val avatar: String,
        @SerializedName("date") val date: String,
        @SerializedName("comment") val comment: String
    ) {
        companion object {
            fun create(json: String): DataField {
                val gson = GsonBuilder().create()
                return gson.fromJson(json, DataField::class.java)
            }
        }

        override fun toString(): String {
            val gson = GsonBuilder().create()
            return gson.toJson(this)
        }
    }
}