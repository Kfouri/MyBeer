package com.kfouri.mybeer.ui.fragment

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kfouri.mybeer.model.ActivityModel
import com.kfouri.mybeer.model.SnackBarModel
import com.kfouri.mybeer.util.Utils
import com.kfouri.mybeer.viewmodel.BaseViewModel

open class BaseFragment : Fragment() {

    lateinit var viewModel: BaseViewModel

    fun subscribe() {
        viewModel.onStartActivity().observe(this, Observer { startActivityModel(it) })
        viewModel.onShowSnackBar().observe(this, Observer { showSnackBar(it) })
        viewModel.onHideKeyboard().observe(this, Observer { hideKeyboard() })
        viewModel.onShowToast().observe(this, Observer { showToast(it) })
        viewModel.onCloseActivity().observe(this, Observer { closeActivity() })
    }

    protected fun startActivityModel(activityModel: ActivityModel) {
        if (activityModel.bundle != null) {
            val intent = Intent(activity, activityModel.activity).putExtras(activityModel.bundle)
            if (activityModel.resultCode > 0) {
                startActivityForResult(intent, activityModel.resultCode)
            } else {
                startActivity(intent)
            }
        } else {
            val intent = Intent(activity, activityModel.activity)
            activityModel.bundle?.let { intent.putExtras(it) }
            if (activityModel.resultCode > 0) {
                startActivityForResult(intent, activityModel.resultCode)
            } else {
                startActivity(intent)
            }
        }
    }

    private fun hideKeyboard() {
        activity?.let { Utils.hideKeyboard(it) }
    }

    private fun showSnackBar(snackBarModel: SnackBarModel) {
        val snackbar = activity?.window?.decorView?.rootView?.let {
            Snackbar
                .make(it, snackBarModel.text, Snackbar.LENGTH_LONG)
        }
        snackbar?.show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(activity, getString(text), Toast.LENGTH_LONG).show()
    }

    private fun closeActivity() {
        activity?.finish()
    }
}