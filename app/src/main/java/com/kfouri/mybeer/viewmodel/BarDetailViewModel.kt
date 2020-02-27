package com.kfouri.mybeer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kfouri.mybeer.R
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.network.model.BarRequest
import com.kfouri.mybeer.network.model.RatingBarBody
import com.kfouri.mybeer.network.model.RatingBarResponse
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.util.Utils
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class BarDetailViewModel : BaseViewModel() {

    var barData = MutableLiveData<BarModel>()

    fun getBar(barId: Int) {
        val userId = PrefsHelper.read(PrefsHelper.ID, 0)
        val userLat = PrefsHelper.read(PrefsHelper.LAT, 0.0)
        val userLot = PrefsHelper.read(PrefsHelper.LON, 0.0)

        mAPIService?.getBar(BarRequest(barId, userId, userLat, userLot))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { showLoading() }
            ?.doOnTerminate { hideLoading() }
            ?.subscribe(object : Subscriber<BarModel>() {
                override fun onCompleted() {

                }
                override fun onError(e: Throwable) {
                    Utils.getErrorBody(e)?.let {
                        showToast(getErrorText(it.code))
                    } ?: showToast(getErrorText("99"))
                    hideLoading()
                }
                override fun onNext(response: BarModel) {
                    barData.value = response
                }
            })
    }

    fun ratingBar(barId: Int, value: Float) {
        val userId = PrefsHelper.read(PrefsHelper.ID, 0)
        mAPIService?.ratingBar(RatingBarBody(barId, userId, value))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { showLoading() }
            ?.doOnTerminate { hideLoading() }
            ?.subscribe(object : Subscriber<RatingBarResponse>() {
                override fun onCompleted() {
                    showToast(getErrorText("03"))
                    getBar(barId)
                }
                override fun onError(e: Throwable) {
                    Utils.getErrorBody(e)?.let {
                        showToast(getErrorText(it.code))
                    } ?: showToast(getErrorText("99"))
                    hideLoading()
                }
                override fun onNext(response: RatingBarResponse) {

                }
            })
    }

    fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.register_user_error_empty_fields
            "02" -> R.string.error_no_authorized
            "03" -> R.string.rating_bar_successful
            else -> R.string.register_user_error_server_error
        }
    }

    fun onBarData() = barData
}