package com.kfouri.mybeer.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun onClickLogin() {
        Log.d("Kfouri", "Login")
    }

    fun onClickRegister() {
        Log.d("Kfouri", "Reg")
    }
}