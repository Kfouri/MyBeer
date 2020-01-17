package com.kfouri.mybeer.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.ui.RegisterUserActivity

class LoginViewModel : BaseViewModel() {

    fun onClickLogin() {
        Log.d("Kfouri", "Login")
    }

    fun onClickRegister() {
        showActivity(ActivityModel(RegisterUserActivity::class.java))
    }
}