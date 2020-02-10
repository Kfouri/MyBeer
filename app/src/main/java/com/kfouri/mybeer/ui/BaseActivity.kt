package com.kfouri.mybeer.ui

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.model.SnackBarModel
import com.kfouri.mybeer.utils.Utils
import com.kfouri.mybeer.viewmodels.BaseViewModel
import androidx.lifecycle.Observer

open class BaseActivity : AppCompatActivity() {

    lateinit var viewModel: BaseViewModel

    fun subscribe() {
        viewModel.onStartActivity().observe(this, Observer { startActivityModel(it) })
        viewModel.onShowSnackBar().observe(this, Observer { showSnackBar(it) })
        viewModel.onHideKeyboard().observe(this, Observer { hideKeyboard() })
        viewModel.onShowToast().observe(this, Observer { showToast(it) })
        viewModel.onCloseActivity().observe(this, Observer { closeActivity() })
    }

    private fun startActivityModel(activityModel: ActivityModel) {
        activityModel.bundle?.let {
            if (activityModel.resultCode > 0) {
                startActivityForResult(Intent(this, activityModel.activity), activityModel.resultCode)
            } else {
                startActivity(Intent(this, activityModel.activity))
            }
        } ?: run {
            val intent = Intent(this, activityModel.activity)
            activityModel.bundle?.let { intent.putExtras(it) }
            if (activityModel.resultCode > 0) {
                startActivityForResult(intent, activityModel.resultCode)
            } else {
                startActivity(intent)
            }
        }
    }

    private fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }

    private fun showSnackBar(snackBarModel: SnackBarModel) {
        val snackbar = Snackbar
            .make(window.decorView.rootView, snackBarModel.text, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(this, getString(text), Toast.LENGTH_LONG).show()
    }

    private fun closeActivity() {
        finish()
    }
}
