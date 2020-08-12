package vn.hexagon.vietnhat.data.model.fone

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeRangeResponse(

	@field:SerializedName("data")
	val data: List<TimeRange?>? = null,

	@field:SerializedName("errorId")
	val errorId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class TimeRange(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("time_start")
	val timeStart: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("time_end")
	val timeEnd: String? = null
) : Parcelable
