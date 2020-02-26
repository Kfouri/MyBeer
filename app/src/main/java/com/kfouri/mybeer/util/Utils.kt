package com.kfouri.mybeer.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

    fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}