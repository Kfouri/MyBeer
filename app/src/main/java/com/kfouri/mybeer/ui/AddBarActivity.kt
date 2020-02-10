package com.kfouri.mybeer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityAddBarBinding
import com.kfouri.mybeer.viewmodels.AddBarViewModel
import com.kfouri.mybeer.viewmodels.FIND_IN_MAP_RESULT_CODE
import kotlinx.android.synthetic.main.activity_add_bar.*

class AddBarActivity : BaseActivity() {

    private lateinit var binding: ActivityAddBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBarViewModel::class.java)
        subscribe()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bar)
        binding.viewmodel = viewModel as AddBarViewModel
        binding.lifecycleOwner = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FIND_IN_MAP_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val address: String = data?.getStringExtra(POSITION_ADDRESS) ?: ""
                val city: String = data?.getStringExtra(POSITION_CITY) ?: ""
                val country: String = data?.getStringExtra(POSITION_COUNTRY) ?: ""
                val lat: Double = data?.getDoubleExtra(POSITION_LAT, 0.0) ?: 0.0
                val lon: Double = data?.getDoubleExtra(POSITION_LON, 0.0) ?: 0.0

                editText_address.text = Editable.Factory.getInstance().newEditable(address)
                editText_city.text = Editable.Factory.getInstance().newEditable(city)
                editText_country.text = Editable.Factory.getInstance().newEditable(country)
                editText_lat.text = Editable.Factory.getInstance().newEditable(String.format("%.8f", lat))
                editText_lon.text = Editable.Factory.getInstance().newEditable(String.format("%.8f", lon))
            }
        }
    }
}