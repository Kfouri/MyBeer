package com.kfouri.mybeer.ui

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.model.SnackBarModel
import com.kfouri.mybeer.utils.Utils

open class BaseActivity : AppCompatActivity() {

    fun startActivityModel(activityModel: ActivityModel) {
        activityModel.bundle?.let {
            startActivity(Intent(this, activityModel.activity))
        } ?: run {
            val intent = Intent(this, activityModel.activity)
            activityModel.bundle?.let { intent.putExtras(it) }
            startActivity(intent)
        }
    }

    fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }

    fun showSnackBar(snackBarModel: SnackBarModel) {
        val snackbar = Snackbar
            .make(window.decorView.rootView, snackBarModel.text, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    fun showToast(text: Int) {
        Toast.makeText(this, getString(text), Toast.LENGTH_LONG).show()
    }

    fun closeActivity() {
        finish()
    }
}
