package com.kfouri.mybeer.ui

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Handler
import com.kfouri.mybeer.R
import com.kfouri.mybeer.utils.PrefsHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val REQUEST_LOCATION = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        PrefsHelper.init(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                val snackbar = Snackbar
                    .make(constraintLayout, getString(R.string.splash_error_permission_denied), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.splash_error_permission_retry)) {
                        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION)
                    }
                snackbar.show()
            }
        }
    }
    private fun getLastLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                PrefsHelper.write(PrefsHelper.LAT, location?.latitude?.let { it } ?: 0.0)
                PrefsHelper.write(PrefsHelper.LON, location?.longitude?.let { it } ?: 0.0)
                Handler().postDelayed({
                    if (!PrefsHelper.read(PrefsHelper.BEARER,"").isNullOrBlank()) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    finish()
                },2000)

            }
    }
}