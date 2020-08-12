package vn.hexagon.vietnhat.ui.list.phone

import androidx.lifecycle.MutableLiveData
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.fone.Brand
import vn.hexagon.vietnhat.data.model.fone.BrandResponse
import vn.hexagon.vietnhat.data.model.fone.FoneDetailResponse
import vn.hexagon.vietnhat.data.model.fone.ProductModelResponse
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.repository.detail.FoneHouseDetailRepository
import java.util.*
import javax.inject.Inject

class FoneSearchViewModel @Inject constructor(private val repository: FoneHouseDetailRepository) :
    MVVMBaseViewModel()  {

    val foneBrandResponse = MutableLiveData<BrandResponse>()
    val foneModelResponse = MutableLiveData<ProductModelResponse>()


    fun getBrand(userId: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.getBrand(userId)
            .applyScheduler()
            .subscribe({ result ->
                networkState.postValue(NetworkState.LOADED)
                foneBrandResponse.postValue(result)
            }, { throwable ->
                networkState.postValue(NetworkState.ERROR)
                DebugLog.e(throwable.message.toString())
            }).addToCompositeDisposable(compositeDisposable)
    }

    fun getModel(brandID: String?) {
        networkState.postValue(NetworkState.LOADING)
        repository.getModel(brandID)
            .applyScheduler()
            .subscribe({ result ->
                networkState.postValue(NetworkState.LOADED)
                foneModelResponse.postValue(result)
            }, { throwable ->
                networkState.postValue(NetworkState.ERROR)
                DebugLog.e(throwable.message.toString())
            }).addToCompositeDisposable(compositeDisposable)
    }

}