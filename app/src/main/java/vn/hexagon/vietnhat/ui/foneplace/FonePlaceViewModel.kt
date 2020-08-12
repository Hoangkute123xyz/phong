package vn.hexagon.vietnhat.ui.foneplace

import androidx.lifecycle.MediatorLiveData
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.warrant.WarrantResponse
import javax.inject.Inject

class FonePlaceViewModel @Inject constructor(private val repository: FonePlaceRepository) :
    MVVMBaseViewModel() {


    val warrantResponse = MediatorLiveData<WarrantResponse>()
    fun getWarrant(userID: String, phone: String) {
        repository.getWarranty(userID, phone).applyScheduler()
            .subscribe(
                { response ->
                    warrantResponse.postValue(response)
                }, { throwable ->
                    throwable.printStackTrace()
                }
            ).addToCompositeDisposable(compositeDisposable)
    }

}