package com.kfouri.mybeer.ui

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.kfouri.mybeer.R
import com.kfouri.mybeer.util.PrefsHelper
import kotlinx.android.synthetic.main.activity_position_bar_map.*
import java.util.*

const val POSITION_ADDRESS = "address"
const val POSITION_CITY = "city"
const val POSITION_COUNTRY = "country"
const val POSITION_LAT = "lat"
const val POSITION_LON = "lon"

class PositionBarMapActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private var lastLat: Double = 0.0
    private var lastLon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_position_bar_map)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
        mapFragment?.getMapAsync(this)

        button_done.setOnClickListener {
            val returnIntent = intent
            returnIntent.putExtra(POSITION_ADDRESS, editText_address.text.toString())
            returnIntent.putExtra(POSITION_CITY, editText_city.text.toString())
            returnIntent.putExtra(POSITION_COUNTRY, editText_country.text.toString())
            returnIntent.putExtra(POSITION_LAT, lastLat)
            returnIntent.putExtra(POSITION_LON, lastLon)

            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        map = googleMap ?: return

        googleMap.setOnCameraIdleListener(this)

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(PrefsHelper.read(PrefsHelper.LAT, 0.0), PrefsHelper.read(PrefsHelper.LON, 0.0)),
                12f
            )
        )
    }

    override fun onCameraIdle() {
        map.cameraPosition?.target?.let {
            getAddressFromPosition(it.latitude , it.longitude)
        }
    }

    private fun getAddressFromPosition(lat: Double, lon: Double) {

        val addresses: List<Address?>

        val geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(lat, lon, 1)

        var address = ""
        var city = ""
        var country = ""
        var street = ""
        var number = ""
        lastLat = lat
        lastLon = lon

        if (addresses.isNotEmpty()) {
            try {
                if (addresses[0]?.thoroughfare != "null") {
                    street = addresses[0]?.thoroughfare!!
                }

                if (addresses[0]?.subThoroughfare != "null") {
                    number = addresses[0]?.subThoroughfare!!
                }

                address = street + " " + number
                city = addresses[0]!!.locality
                country = addresses[0]!!.countryName
            } catch (e: Exception) {}
        }

        editText_address.text = Editable.Factory.getInstance().newEditable(address)
        editText_city.text = Editable.Factory.getInstance().newEditable(city)
        editText_country.text = Editable.Factory.getInstance().newEditable(country)
    }
}
