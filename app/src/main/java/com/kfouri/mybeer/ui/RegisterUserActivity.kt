package com.kfouri.mybeer.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityRegisterUserBinding
import com.kfouri.mybeer.viewmodels.RegisterUserViewModel

class RegisterUserActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterUserViewModel::class.java)
        subscribe()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_user)
        binding.viewmodel = viewModel as RegisterUserViewModel
        binding.lifecycleOwner = this
    }
}
