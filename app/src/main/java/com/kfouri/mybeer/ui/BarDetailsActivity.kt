package com.kfouri.mybeer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kfouri.mybeer.R
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.util.Utils.bitmapDescriptorFromVector
import kotlinx.android.synthetic.main.activity_bar_details.*

class BarDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private lateinit var bar: BarModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_details)
        bar = (intent.extras?.getSerializable("bar") as BarModel)

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
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(bar.lat, bar.lon), 16f))

        map.addMarker(
            MarkerOptions()
                .position(LatLng(bar.lat, bar.lon))
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_room))
        )

        map.addMarker(
            MarkerOptions()
                .position(LatLng(PrefsHelper.read(PrefsHelper.LAT, 0.0), PrefsHelper.read(PrefsHelper.LON, 0.0)))
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_you))
        )
    }
}
