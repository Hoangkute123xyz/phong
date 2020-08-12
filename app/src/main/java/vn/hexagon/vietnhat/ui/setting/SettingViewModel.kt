package vn.hexagon.vietnhat.ui.setting

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.base.mvvm.MVVMBaseViewModel
import vn.hexagon.vietnhat.base.utils.DebugLog
import vn.hexagon.vietnhat.base.utils.addToCompositeDisposable
import vn.hexagon.vietnhat.base.utils.applyScheduler
import vn.hexagon.vietnhat.constant.Constant
import vn.hexagon.vietnhat.data.model.auth.LoginResponse
import vn.hexagon.vietnhat.repository.info.UserInfoRepository
import javax.inject.Inject

/**
 *
//                       _ooOoo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                       O\ = /O
//                   ____/`---'\____
//                 .   ' \\| |// `.
//                  / \\||| : |||// \
//                / _||||| -:- |||||- \
//                  | | \\\ - /// | |
//                | \_| ''\---/'' | |
//                 \ .-\__ `-` ___/-. /
//              ______`. .' /--.--\ `. . __
//           ."" '< `.___\_<|>_/___.' >'"".
//          | | : `- \`.;`\ _ /`;.`/ - ` : | |
//            \ \ `-. \_ __\ /__ _/ .-` / /
//    ======`-.____`-.___\_____/___.-`____.-'======
//                       `=---='
//
//    .............................................
//                    Pray for no Bugs
 * =====================================================
 * Name：VuNBT
 * Create on：2019-09-10
 * =====================================================
 */
class SettingViewModel @Inject constructor(private val repository: UserInfoRepository): MVVMBaseViewModel() {
    // User info response
    val userInfoResponse = MutableLiveData<LoginResponse>()

    fun getImageBanner(): String {
        return "https://image.freepik.com/free-photo/pink-bokeh-blurred-abstract-background_3249-2045.jpg"
    }

    fun getUserInfo(userId: String) {
        repository.getUserInfo(userId)
            .applyScheduler()
            .subscribe(
                { result -> userInfoResponse.postValue(result) },
                { throwable -> DebugLog.e(throwable.message.toString()) }
            ).addToCompositeDisposable(compositeDisposable)
    }
}