package vn.hexagon.vietnhat.ui.list.phone

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.MutableLiveData
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.fone.FoneDetailResponse
import vn.hexagon.vietnhat.data.model.fone.TimeRangeResponse
import vn.hexagon.vietnhat.data.model.gift.PhoneGiftDetailResponse
import vn.hexagon.vietnhat.data.model.service.ListPostResponse
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.data.remote.NetworkState.Companion.LOADED
import vn.hexagon.vietnhat.repository.detail.FoneHouseDetailRepository
import javax.inject.Inject

class FoneHouseDetailViewModel @Inject constructor(private val repository: FoneHouseDetailRepository) :
    MVVMBaseViewModel() {

    val foneDetailResponse = MutableLiveData<FoneDetailResponse>()

    val foneGiftDetailResponse = MutableLiveData<PhoneGiftDetailResponse>()

    val favouriteResponse = MutableLiveData<ListPostResponse>()

    val unFavouriteResponse = MutableLiveData<ListPostResponse>()

    val timeRangeResponse = MutableLiveData<TimeRangeResponse>()
    val bookingResponse = MutableLiveData<TimeRangeResponse>()


    fun getFoneDetail(userId: String, productId: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.getDetailPost(userId, productId)
            .applyScheduler()
            .subscribe({ result ->
                networkState.postValue(NetworkState.LOADED)
                foneDetailResponse.postValue(result)
            }, { throwable ->
                networkState.postValue(NetworkState.ERROR)
                DebugLog.e(throwable.message.toString())
            }).addToCompositeDisposable(compositeDisposable)
    }

    fun getWarranty(): String? {
        return String.format("%s %s", "Bảo hành ", foneDetailResponse.value?.data?.warrant)
    }

    fun getLineVisible(): Int {
        if (TextUtils.isEmpty(foneDetailResponse.value?.data?.priceDiscount)) {
            return View.GONE
        } else
            return View.VISIBLE
    }


    // * Get Phone gift detail
    // * @param userID: current user id
    // * @param giftID: current selected gift
    fun getPhoneGiftDetail(userId: String, giftID: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.getPhoneGiftDetail(userId, giftID)
            .applyScheduler()
            .subscribe({ result ->
                networkState.postValue(NetworkState.LOADED)
                foneGiftDetailResponse.postValue(result)
            }, { throwable ->
                networkState.postValue(NetworkState.ERROR)
                DebugLog.e(throwable.message.toString())
            }).addToCompositeDisposable(compositeDisposable)
    }

    /*
    * Add gift to favourite
    * @param userID
    * @param giftID
    * */
    fun addFavourite(userID: String, giftID: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.favouriteGift(userID, giftID)
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    favouriteResponse.postValue(result)
                }, { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    DebugLog.e(throwable.message.toString())
                }
            ).addToCompositeDisposable(compositeDisposable)

    }

    /*
        * Remove gift from favourite
        * @param userID
        * @param giftID
        * */
    fun removeFavourite(userID: String, giftID: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.unFavouriteGift(userID, giftID)
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    unFavouriteResponse.postValue(result)
                }, { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    DebugLog.e(throwable.message.toString())
                }
            ).addToCompositeDisposable(compositeDisposable)
    }

    fun getTimeRange() {
        networkState.postValue(NetworkState.LOADING)
        repository.getTimeRange()
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(NetworkState.LOADED)
                    timeRangeResponse.postValue(result)
                }, { throwable ->
                    throwable.printStackTrace()
                    networkState.postValue(NetworkState.ERROR)
                }
            ).addToCompositeDisposable(compositeDisposable)
    }

    fun postOrder(
        userId: String,
        userName: String,
        phone: String,
        address: String,
        email: String,
        productCode: String,
        productPrice: String,
        timeRangeID: String
    ) {
        networkState.postValue(NetworkState.LOADING)
        repository.postBooking(
            userId,
            userName,
            phone,
            address,
            email,
            productCode,
            productPrice,
            timeRangeID
        )
            .applyScheduler()
            .subscribe(
                { result ->
                    networkState.postValue(LOADED)
                    bookingResponse.postValue(result)
                }, { throwable ->
                    networkState.postValue(NetworkState.ERROR)
                    throwable.printStackTrace()
                }
            )
            .addToCompositeDisposable(compositeDisposable)
    }
}