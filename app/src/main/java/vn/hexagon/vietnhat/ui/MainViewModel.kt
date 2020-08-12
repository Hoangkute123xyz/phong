package vn.hexagon.vietnhat.ui

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.Utils
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.auth.FcmTokenResponse
import javax.inject.Inject

/**
 * Created by NhamVD on 2019-08-17.
 */
class MainViewModel @Inject constructor(private val repository: MainRepository): MVVMBaseViewModel() {
    // Token response
    val tokenResponse = MutableLiveData<FcmTokenResponse>()
    var currentLocation: Location? = null

    /**
     * Request update token fcm
     *
     * @param userId
     * @param token
     */
    fun requestUpdateToken(userId: String, token: String) {
        repository.updateFcmToken(userId, token)
            .applyScheduler()
            .subscribe(
                { result -> tokenResponse.postValue(result)},
                { throwable -> DebugLog.e(throwable.message.toString())}
            ).addToCompositeDisposable(compositeDisposable)
    }

    fun getCurrentAddr(context: Context) : String{
        currentLocation?.let {
            return Utils.getAddrLatLng(context,it.latitude,it.longitude)?:"Vị trí của bạn"
        }
        return "Vị trí của bạn"
    }
}