package vn.hexagon.vietnhat.ui.home

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.banner.BannerResponse
import vn.hexagon.vietnhat.repository.home.BannerRepository
import javax.inject.Inject

/**
 * Created by NhamVD on 2019-08-17.
 */
class HomeViewModel @Inject constructor(private val repository: BannerRepository) :
    MVVMBaseViewModel() {
    // Banner Response
    val bannerResponse = MutableLiveData<BannerResponse>()
    val locationResponse = MutableLiveData<Location>()

    /**
     * Get app banner
     *
     */
    fun getBanner() {
        repository.getHomeBanner()
            .applyScheduler()
            .subscribe(
                { result -> bannerResponse.postValue(result) },
                { throwable -> DebugLog.e(throwable.message.toString()) }
            ).addToCompositeDisposable(compositeDisposable)
    }

    /**
     * Get current location
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it.let { locationResponse.postValue(it) }
        }
    }

}