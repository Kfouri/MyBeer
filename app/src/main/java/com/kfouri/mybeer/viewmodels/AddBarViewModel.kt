package com.kfouri.mybeer.viewmodels

import androidx.databinding.ObservableField
import com.kfouri.mybeer.R
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.network.model.AddBarBody
import com.kfouri.mybeer.network.model.AddBarResponse
import com.kfouri.mybeer.ui.PositionBarMapActivity
import com.kfouri.mybeer.utils.Utils
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

const val FIND_IN_MAP_RESULT_CODE = 500

class AddBarViewModel : BaseViewModel() {

    var nameField = ObservableField<String>()
    var addressField = ObservableField<String>()
    var cityField = ObservableField<String>()
    var countryField = ObservableField<String>()
    var latField = ObservableField<String>()
    var lonField = ObservableField<String>()

    fun onClickCreate() {
        hideKeyboard()
        if (nameField.get().toString() != "null" &&
            latField.get().toString() != "null" &&
            lonField.get().toString() != "null") {

            mAPIService?.addBar(AddBarBody(nameField.get().toString(),
                                           addressField.get().toString(),
                                           cityField.get().toString(),
                                           countryField.get().toString(),
                                           latField.get()!!.toString(),
                                           lonField.get()!!.toString()))

                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { showLoading() }
                ?.doOnTerminate { hideLoading() }
                ?.subscribe(object : Subscriber<AddBarResponse>() {
                    override fun onCompleted() {
                        closeActivity()
                    }
                    override fun onError(e: Throwable) {
                        Utils.getErrorBody(e)?.let {
                            showToast(getErrorText(it.code))
                        } ?: showToast(getErrorText("99"))
                        hideLoading()
                    }
                    override fun onNext(response: AddBarResponse) {

                    }
                })

        } else {
            showToast(R.string.register_user_error_empty_fields)
        }
    }

    fun onFindBarInMap() {
        showActivity(ActivityModel(PositionBarMapActivity::class.java, null, FIND_IN_MAP_RESULT_CODE))
    }

    fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.add_bar_error
            "02" -> R.string.add_bar_get_position_empty_fields
            else -> R.string.register_user_error_server_error
        }
    }

}