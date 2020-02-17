package com.kfouri.mybeer.viewmodel

import androidx.databinding.ObservableField
import com.kfouri.mybeer.R
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.network.model.CreateUserBody
import com.kfouri.mybeer.network.model.User
import com.kfouri.mybeer.ui.LoginActivity
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.util.Utils
import com.kfouri.mybeer.util.Utils.isValidEmail
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RegisterUserViewModel : BaseViewModel() {

    var emailField = ObservableField<String>()
    var passwordField = ObservableField<String>()
    var nameField = ObservableField<String>()

    fun onClickCreate() {
        hideKeyboard()
        if (!emailField.get().isNullOrEmpty() && !passwordField.get().isNullOrEmpty() && !nameField.get().isNullOrEmpty()) {

            if (!isValidEmail(emailField.get().toString())) {
                showToast(getErrorText("04"))
            } else {
                mAPIService?.createUser(CreateUserBody(emailField.get().toString(), passwordField.get().toString(), nameField.get().toString()))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.doOnSubscribe { showLoading() }
                    ?.doOnTerminate { hideLoading() }
                    ?.subscribe(object : Subscriber<User>() {
                        override fun onCompleted() {
                            showActivity(ActivityModel(LoginActivity::class.java))
                            closeActivity()
                        }
                        override fun onError(e: Throwable) {
                            Utils.getErrorBody(e)?.let {
                                showToast(getErrorText(it.code))
                            } ?: showToast(getErrorText("99"))
                            hideLoading()
                        }
                        override fun onNext(user: User) {
                            PrefsHelper.write(PrefsHelper.EMAIL, user.email)
                        }
                    })
            }

        } else {
            showToast(getErrorText("01"))
        }
    }

    fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.register_user_error_empty_fields
            "02" -> R.string.register_user_error_user_already_exist
            "03" -> R.string.register_user_error_user_not_exist
            "04" -> R.string.register_user_error_invalid_email
            else -> R.string.register_user_error_server_error
        }
    }
}