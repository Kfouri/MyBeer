package com.kfouri.mybeer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kfouri.mybeer.R
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import com.kfouri.mybeer.network.APIService
import com.kfouri.mybeer.network.ApiUtils
import com.kfouri.mybeer.network.model.BarBody
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.utils.PrefsHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mAPIService: APIService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAPIService = ApiUtils.apiService

        getBars()

        button_logout.setOnClickListener {
            PrefsHelper.write(PrefsHelper.REMEMBER, false)
        }
    }

    private fun getBars() {

        val lat = PrefsHelper.read(PrefsHelper.LAT, 0.0).toString()
        val lon = PrefsHelper.read(PrefsHelper.LON, 0.0).toString()
        val radius = "500"

        // RxJava
        mAPIService?.getBars(BarBody(lat, lon, radius))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Subscriber<List<BarModel>>() {
                override fun onCompleted() {
                    Log.d("Kfouri", "Complete")
                }

                override fun onError(e: Throwable) {
                    Log.d("Kfouri", "error "+e.message)
                }

                override fun onNext(bars: List<BarModel>) {
                    for (bar in bars) {
                        Log.d("Kfouri", "Nombre: "+bar.nombre)
                        bar.servicios?.let {
                            for (servicio in it) {
                                Log.d("Kfouri", "Servicio: "+servicio.descripcion)
                            }
                        }
                    }
                }
            })
    }
}
