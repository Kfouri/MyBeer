package com.kfouri.mybeer.ui

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.ActivityBarDetailsBinding
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.util.Utils.bitmapDescriptorFromVector
import com.kfouri.mybeer.viewmodel.BarDetailViewModel
import kotlinx.android.synthetic.main.activity_bar_details.*
import kotlinx.android.synthetic.main.activity_bar_details.ratingBar

class BarDetailsActivity : BaseActivity(), OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private lateinit var bar: BarModel
    private lateinit var binding: ActivityBarDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(BarDetailViewModel::class.java)
        subscribe()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bar_details)
        binding.viewmodel = viewModel as BarDetailViewModel
        binding.lifecycleOwner = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (viewModel as BarDetailViewModel).onBarData().observe(this, Observer { getBarData(it) })
        intent.extras?.let {
            (viewModel as BarDetailViewModel).getBar(it.getInt("idBar", 0))
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(bar.lat, bar.lon), 16f))

        if (bar.id > 0) {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(bar.lat, bar.lon))
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_room))
            )
        }

        map.addMarker(
            MarkerOptions()
                .position(LatLng(PrefsHelper.read(PrefsHelper.LAT, 0.0), PrefsHelper.read(PrefsHelper.LON, 0.0)))
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_you))
        )
    }

    private fun getBarData(data: BarModel) {

        bar = data

        Glide.with(this)
            .load(bar.logo)
            .placeholder(R.drawable.beer_icon)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView_logo)

        textView_name.text = bar.nombre
        textView_address.text = bar.direccion
        textView_city.text = bar.ciudad
        textView_rating.text = bar.rating.toString()
        ratingBar.rating = bar.rating.toFloat()
        textView_votes.text = bar.votes.toString()
        textView_distance.text = bar.distance.toString() + " Km"

        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
        mapFragment?.getMapAsync(this)

        linearLayout_rating.setOnClickListener {
            val ratingDialog = RatingDialog(this, bar.myVote, object: RatingDialog.DialogListener {
                override fun submitClick(value: Float) {
                    (viewModel as BarDetailViewModel).ratingBar(bar.id, value)
                }

                override fun cancelClick() {
                    Toast.makeText(this@BarDetailsActivity, getString(R.string.rating_cancel), Toast.LENGTH_LONG).show()
                }
            })
            ratingDialog.show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
