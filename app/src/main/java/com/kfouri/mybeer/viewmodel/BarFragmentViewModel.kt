package com.kfouri.mybeer.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kfouri.mybeer.R
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.network.model.BarBody
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.ui.AddBarActivity
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.util.Utils
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class BarFragmentViewModel : BaseViewModel() {

    private var barsList = ArrayList<BarModel>()

    var barList = MutableLiveData<ArrayList<BarModel>>()

    fun getBars(radius: Int, lat: String, lon: String) {

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
                    barsList = bars ?: ArrayList()
                }
            })
    }

    fun onBarList() = barList

    fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.register_user_error_empty_fields
            "02" -> R.string.error_no_authorized
            else -> R.string.register_user_error_server_error
        }
    }

    fun onClickAddBar() {
        showActivity(ActivityModel(AddBarActivity::class.java))
    }
}