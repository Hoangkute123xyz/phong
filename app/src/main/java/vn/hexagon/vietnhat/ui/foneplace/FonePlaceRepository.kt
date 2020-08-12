package vn.hexagon.vietnhat.ui.foneplace

import io.reactivex.Single
import vn.hexagon.vietnhat.data.model.warrant.WarrantResponse
import vn.hexagon.vietnhat.data.remote.NetworkService
import javax.inject.Inject

class FonePlaceRepository @Inject constructor(private val apiService: NetworkService)  {

    fun getWarranty(userID:String,phone:String):Single<WarrantResponse>{
        return apiService.getWarrant(userID, phone)
    }

}