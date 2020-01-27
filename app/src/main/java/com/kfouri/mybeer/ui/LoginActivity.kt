package com.kfouri.mybeer.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityLoginBinding
import com.kfouri.mybeer.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        subscribe()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel as LoginViewModel
        binding.lifecycleOwner = this
        Glide.with(this)
             .load(R.drawable.photo)
             .apply(RequestOptions.circleCropTransform())
             .into(imageView)
    }

    override fun onResume() {
        super.onResume()
        (viewModel as LoginViewModel).setView()
    }
}
