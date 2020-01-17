package com.kfouri.mybeer.utils

import android.app.Activity
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.kfouri.mybeer.network.model.ErrorBody
import okhttp3.ResponseBody
import java.io.IOException


object Utils {

    fun getErrorBody(error: Throwable): ErrorBody? {
        var errorMessage: ErrorBody? = null
        if (error is retrofit2.adapter.rxjava.HttpException) {
            val body: ResponseBody? = error.response().errorBody()
            val gson = Gson()
            val adapter: TypeAdapter<ErrorBody> = gson.getAdapter<ErrorBody>(ErrorBody::class.java)
            try {
                errorMessage = adapter.fromJson(body!!.string())
            } catch (e: IOException) {
                errorMessage?.code = "99"
                errorMessage?.message = e.message.toString()
            }
        }
        return errorMessage
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}