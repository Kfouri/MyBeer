package com.kfouri.mybeer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.model.SnackBarModel
import com.kfouri.mybeer.network.APIService
import com.kfouri.mybeer.network.ApiUtils

open class BaseViewModel : ViewModel() {

    var mAPIService: APIService? = null
    var startActivity = MutableLiveData<ActivityModel>()
    var showSnackBar = MutableLiveData<SnackBarModel>()
    var hideKeyboard = MutableLiveData<Unit>()
    var showToast = MutableLiveData<Int>()
    var progress = MutableLiveData<Int>()
    var closeActivity = MutableLiveData<Unit>()

    init {
        hideLoading()
        mAPIService = ApiUtils.apiService
    }

    fun showLoading() {
        progress.value = 0
    }

    fun hideLoading() {
        progress.value = 8
    }

    fun closeActivity() {
        closeActivity.value = Unit
    }

    fun showActivity(activityModel: ActivityModel) {
        startActivity.value = activityModel
    }

    fun showSnackBar(snackBarModel: SnackBarModel) {
        showSnackBar.value = snackBarModel
    }

    fun hideKeyboard() {
        hideKeyboard.value = Unit
    }

    fun showToast(text: Int) {
        showToast.value = text
    }

    fun onStartActivity() = startActivity
    fun onShowSnackBar() = showSnackBar
    fun onHideKeyboard() = hideKeyboard
    fun onShowToast() = showToast
    fun onCloseActivity() = closeActivity

}