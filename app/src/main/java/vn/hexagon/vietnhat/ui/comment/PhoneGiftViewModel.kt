package vn.hexagon.vietnhat.ui.comment

import androidx.lifecycle.MutableLiveData
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.data.model.comment.ListFavoritePhoneResponse
import vn.hexagon.vietnhat.data.model.fone.FoneComment
import vn.hexagon.vietnhat.data.remote.NetworkState
import vn.hexagon.vietnhat.repository.detail.FoneHouseDetailRepository
import javax.inject.Inject

class PhoneGiftViewModel @Inject constructor(private val repository: FoneHouseDetailRepository) :
    MVVMBaseViewModel() {


    var listFavoritePhoneResponse = MutableLiveData<ListFavoritePhoneResponse>()
    var listFoneHouseCommentResponse = MutableLiveData<FoneComment>()

    fun getPhoneGiftComment(giftID: String?) {

    }

    fun getPhoneGiftFavourite(giftID: String?) {

    }

    fun getPhoneHouseComment(phoneID: String) {
        repository.getCommentFoneHouse(phoneID).applyScheduler().subscribe({response->
            listFoneHouseCommentResponse.postValue(response)
        },{throwable-> throwable.printStackTrace()}).addToCompositeDisposable(compositeDisposable)
    }

    fun getPhoneHouseFavorite(phoneID: String?) {
        phoneID?.let {
            repository.getListFavoritePhone(it).applyScheduler().subscribe({ response ->
                listFavoritePhoneResponse.postValue(response)
            }, { throwable -> throwable.printStackTrace() })
                .addToCompositeDisposable(compositeDisposable)
        }
    }

    fun postCommentPhoneHouse(content: String, giftID: String, user_id: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.commentPhone(giftID, user_id, 1, content)
            .applyScheduler()
            .subscribe({ result ->
                networkState.postValue(NetworkState.LOADED)
                if (result.message.equals("Success")) {
                    getPhoneHouseComment(giftID)
                }
            }, { throwable ->
                networkState.postValue(NetworkState.ERROR)
                DebugLog.e(throwable.message.toString())
            }).addToCompositeDisposable(compositeDisposable)
    }

    fun postCommentPhoneGift(content: String, giftID: String, user_id: String) {
        networkState.postValue(NetworkState.LOADING)
        repository.commentPhone(giftID, user_id, 2, content)
            .applyScheduler()
            .subscribe({ result ->
                networkState.postValue(NetworkState.LOADED)
                if (result.message.equals("Success")) {
                    getPhoneGiftComment(giftID)
                }
            }, { throwable ->
                networkState.postValue(NetworkState.ERROR)
                DebugLog.e(throwable.message.toString())
            }).addToCompositeDisposable(compositeDisposable)
    }
}