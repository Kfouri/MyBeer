package com.kfouri.mybeer.viewmodels

import androidx.lifecycle.MutableLiveData
import com.kfouri.mybeer.R
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.network.model.BarBody
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.ui.AddBarActivity
import com.kfouri.mybeer.utils.PrefsHelper
import com.kfouri.mybeer.utils.Utils
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class BarFragmentViewModel : BaseViewModel() {

    private var barsList = ArrayList<BarModel>()

    var barList = MutableLiveData<ArrayList<BarModel>>()

    fun getBars(radius: Int) {
        val lat = PrefsHelper.read(PrefsHelper.LAT, 0.0).toString()
        val lon = PrefsHelper.read(PrefsHelper.LON, 0.0).toString()

        mAPIService?.getBars(BarBody(lat, lon, radius.toString()))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { showLoading() }
            ?.doOnTerminate { hideLoading() }
            ?.subscribe(object : Subscriber<ArrayList<BarModel>>() {
                override fun onCompleted() {
                    barList.value = barsList
                }

                override fun onError(e: Throwable) {
                    Utils.getErrorBody(e)?.let {
                        showToast(getErrorText(it.code))
                    } ?: showToast(getErrorText("99"))
                    hideLoading()
                }

                override fun onNext(bars: ArrayList<BarModel>?) {
                    if (bars != null) {
                        barsList = bars
                    }
                }
            })
    }

    fun onBarList() = barList

    fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.register_user_error_empty_fields //"Parameters not setted"
            "02" -> R.string.login_error_email_password_incorrect //No authorizated
            else -> R.string.register_user_error_server_error
        }
    }

    fun onClickAddBar() {
        showActivity(ActivityModel(AddBarActivity::class.java))
    }
}