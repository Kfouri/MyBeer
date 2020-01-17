package com.kfouri.mybeer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityLoginBinding
import com.kfouri.mybeer.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel

    }
}
