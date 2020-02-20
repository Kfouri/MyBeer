package com.kfouri.mybeer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kfouri.mybeer.R
import com.kfouri.mybeer.databinding.FragmentMapBinding
import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.util.PrefsHelper
import com.kfouri.mybeer.viewmodel.BarFragmentViewModel
import kotlinx.android.synthetic.main.custom_marker.*
import kotlinx.android.synthetic.main.custom_marker.view.*
import org.json.JSONException
import org.json.JSONObject


class MapFragment : BaseFragment(), OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentMapBinding

    private val markerMap = HashMap<Marker, BarModel>()

    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(BarFragmentViewModel::class.java)
        (viewModel as BarFragmentViewModel).onBarList().observe(this, Observer { getBarList(it) })
        subscribe()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.viewmodel = viewModel as BarFragmentViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextRadius.text = Editable.Factory.getInstance().newEditable("$DEFAULT_RADIUS Km")

        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
        mapFragment?.getMapAsync(this)

        binding.buttonGetBars.setOnClickListener {
            map.cameraPosition?.target?.let {
                map.clear()
                val radius = binding.editTextRadius.text.toString().replace(" Km","")
                (viewModel as BarFragmentViewModel).getBars(radius.toInt(), it.latitude.toString(), it.longitude.toString())
            }
        }

        binding.buttonSetRadius.setOnClickListener {
            val kms = arrayOf("1", "2", "5", "10", "20", "30", "50", "100")

            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.main_activity_dialog_title))
                .setIcon(R.drawable.beer_icon)

            builder.setItems(kms) { _, which ->

                map.cameraPosition?.target?.let {
                    map.clear()
                    binding.editTextRadius.text = Editable.Factory.getInstance().newEditable(kms[which] + " Km")
                    (viewModel as BarFragmentViewModel).getBars(kms[which].toInt(), it.latitude.toString(), it.longitude.toString())
                }

            }
            builder.setNegativeButton(getString(R.string.cancel), null)
            builder.show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        map = googleMap ?: return

        map.uiSettings.isZoomControlsEnabled = true

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(PrefsHelper.read(PrefsHelper.LAT, 0.0), PrefsHelper.read(PrefsHelper.LON, 0.0)),
                12f
            )
        )

        val lat = PrefsHelper.read(PrefsHelper.LAT, 0.0).toString()
        val lon = PrefsHelper.read(PrefsHelper.LON, 0.0).toString()
        val rad = PrefsHelper.read(PrefsHelper.RADIUS, DEFAULT_RADIUS)

        (viewModel as BarFragmentViewModel).getBars(rad, lat, lon)

        map.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker?): View {
                val customMarker = layoutInflater.inflate(R.layout.custom_marker, null)

                val bar = markerMap[marker]

                customMarker.name.text = bar?.nombre
                customMarker.address.text = bar?.direccion
                customMarker.city.text = bar?.ciudad
                customMarker.distance.text = bar?.distance.toString() + " Km"

                return customMarker
            }

            override fun getInfoWindow(marker: Marker?): View? {
                return null
            }

        })
    }

    private fun getBarList(list: ArrayList<BarModel>) {
        map.clear()

        list.forEach { it ->

            val bar = BarModel(0, it.nombre, it.direccion, it.ciudad, it.lat, it.lon, it.logo, it.distance, ArrayList())

            val marker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.lon))
                    .title(it.nombre)
                    .snippet(it.direccion + "\n" + it.ciudad + "\n" + it.distance + " Km")
                    .icon(activity?.let { bitmapDescriptorFromVector(it, R.drawable.ic_room) })
            )

            markerMap[marker] = bar
        }

    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}