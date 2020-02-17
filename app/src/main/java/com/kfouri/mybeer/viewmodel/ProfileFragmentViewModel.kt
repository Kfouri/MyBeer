package com.kfouri.mybeer.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.kfouri.mybeer.R
import com.kfouri.mybeer.network.model.OptionProfileModel
import com.kfouri.mybeer.network.model.User
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.util.Utils
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

const val OPTION_EXIT = "option_exit"
const val OPTION_INVITE_FRIENDS = "option_invite_friends"
const val OPTION_HELP = "option_help"

class ProfileFragmentViewModel : BaseViewModel() {

    private var list = ArrayList<OptionProfileModel>()
    private var optionProfileList = MutableLiveData<ArrayList<OptionProfileModel>>()

    val userName = ObservableField<String>()

    init {
        setOptionList()
        getUser()
    }

    private fun setOptionList() {
        //list.add(OptionProfileModel("Ayuda", "Preguntas frecuentes, contáctanos, politicas", R.drawable.ic_help, OPTION_HELP))
        //list.add(OptionProfileModel("Invitar amigos", "", R.drawable.ic_group, OPTION_INVITE_FRIENDS))
        list.add(OptionProfileModel("Logout", "Salir de la aplicación", R.drawable.ic_exit, OPTION_EXIT))

        optionProfileList.value = list
    }

    private fun getUser() {
        mAPIService?.getUser(PrefsHelper.read(PrefsHelper.TOKEN, "")!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { showLoading() }
            ?.doOnTerminate { hideLoading() }
            ?.subscribe(object : Subscriber<User>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    Utils.getErrorBody(e)?.let {
                        showToast(getErrorText(it.code))
                    } ?: showToast(getErrorText("99"))
                    hideLoading()
                }

                override fun onNext(user: User?) {
                    if (user != null) {
                        userName.set(user.nombre)
                    }
                }
            })
    }

    private fun getErrorText(code: String): Int {
        return when (code) {
            "01" -> R.string.register_user_error_empty_fields
            "02" -> R.string.register_user_error_user_not_exist
            "03" -> R.string.login_error_email_password_incorrect //No authorizated
            else -> R.string.register_user_error_server_error
        }
    }

    fun onOptionProfileList() = optionProfileList
}