package com.kfouri.mybeer.viewmodels

import androidx.databinding.ObservableField
import com.kfouri.mybeer.R
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.network.APIService
import com.kfouri.mybeer.network.ApiUtils
import com.kfouri.mybeer.network.model.LoginBody
import com.kfouri.mybeer.network.model.User
import com.kfouri.mybeer.ui.MainActivity
import com.kfouri.mybeer.ui.RegisterUserActivity
import com.kfouri.mybeer.utils.PrefsHelper
import com.kfouri.mybeer.utils.Utils
import com.kfouri.mybeer.utils.Utils.isValidEmail
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LoginViewModel : BaseViewModel() {

    private var mAPIService: APIService? = null

    var emailField = ObservableField<String>()
    var passwordField = ObservableField<String>()
    var rememberField = ObservableField<Boolean>()

    init {
        hideLoading()
        mAPIService = ApiUtils.apiService
    }

    fun setView() {
        val email = PrefsHelper.read(PrefsHelper.EMAIL, "").toString()
        if (email.isNotEmpty()) {
            emailField.set(email)
        }
        passwordField.set("")
    }

    fun onClickLogin() {
        if (!emailField.get().isNullOrEmpty() && !passwordField.get().isNullOrEmpty()) {

            if (!isValidEmail(emailField.get().toString())) {
                showToast(getErrorText("03"))
            } else {
                mAPIService?.login(LoginBody(emailField.get().toString(), passwordField.get().toString()))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.doOnSubscribe { showLoading() }
                    ?.doOnTerminate { hideLoading() }
                    ?.subscribe(object : Subscriber<User>() {
                        override fun onCompleted() {
                            showActivity(ActivityModel(MainActivity::class.java))
                            PrefsHelper.write(PrefsHelper.REMEMBER, rememberField.get() == true)
                            closeActivity()
                        }
                        override fun onError(e: Throwable) {
                            Utils.getErrorBody(e)?.let {
                                showToast(getErrorText(it.code))
                            } ?: showToast(getErrorText("99"))
                            hideLoading()
                        }
                        override fun onNext(user: User) {
                            PrefsHelper.write(PrefsHelper.ID, user.id)
                            PrefsHelper.write(PrefsHelper.EMAIL, user.email)
                            PrefsHelper.write(PrefsHelper.TOKEN, user.token)
                        }
                    })
            }

        } else {
            showToast(getErrorText("01"))
        }
    }

    fun onClickRegister() {
        showActivity(ActivityModel(RegisterUserActivity::class.java))
    }

    fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.register_user_error_empty_fields
            "02" -> R.string.login_error_email_password_incorrect
            "03" -> R.string.register_user_error_invalid_email
            else -> R.string.register_user_error_server_error
        }
    }
}