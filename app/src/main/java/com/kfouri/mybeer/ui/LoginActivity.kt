package com.kfouri.mybeer.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityLoginBinding
import com.kfouri.mybeer.viewmodels.LoginViewModel

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.onStartActivity().observe(this, Observer { startActivityModel(it) })
        viewModel.onShowSnackBar().observe(this, Observer { showSnackBar(it) })
        viewModel.onHideKeyboard().observe(this, Observer { hideKeyboard() })
        viewModel.onShowToast().observe(this, Observer { showToast(it) })
        viewModel.onCloseActivity().observe(this, Observer { closeActivity() })
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onResume() {
        super.onResume()
        viewModel.setView()
    }
}
