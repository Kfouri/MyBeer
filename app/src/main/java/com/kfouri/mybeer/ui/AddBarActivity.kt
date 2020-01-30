package com.kfouri.mybeer.ui

import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityAddBarBinding
import com.kfouri.mybeer.viewmodels.AddBarViewModel
import kotlinx.android.synthetic.main.activity_add_bar.*
import java.io.IOException

class AddBarActivity : BaseActivity() {

    private lateinit var binding: ActivityAddBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBarViewModel::class.java)
        subscribe()
        (viewModel as AddBarViewModel).onGetLocationFromAddress().observe(this, Observer { getLocationFromAddress(it) })
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bar)
        binding.viewmodel = viewModel as AddBarViewModel
        binding.lifecycleOwner = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getLocationFromAddress(strAddress: String) {
        val coder = Geocoder(this)
        val address = coder.getFromLocationName(strAddress,5)

        editText_lat.text = Editable.Factory.getInstance().newEditable("")
        editText_lon.text = Editable.Factory.getInstance().newEditable("")

        try {
            if (address != null && address.size > 0) {
                val location = address[0]
                editText_lat.text = Editable.Factory.getInstance().newEditable(String.format("%.8f", location?.latitude))
                editText_lon.text = Editable.Factory.getInstance().newEditable(String.format("%.8f", location?.longitude))
            } else {
                Toast.makeText(this, getString(R.string.add_bar_error_get_position), Toast.LENGTH_LONG).show()
            }
        } catch (ex: IOException) {
            Toast.makeText(this, getString(R.string.add_bar_error_get_position), Toast.LENGTH_LONG).show()
        }
    }
}