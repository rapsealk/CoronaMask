package com.rapsealk.coronamask.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rapsealk.coronamask.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var googleMap: GoogleMap

    private val markers = ArrayList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (PERMISSIONS.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_REQ_CODE)
        }

        viewModel.stores.observe(this) { stores ->
            markers.forEach { it.remove() }
            markers.clear()
            for (store in stores.filterNotNull()) {
                Log.d(TAG, "Store: $store")
                val markerOptions = MarkerOptions()
                    .position(store.getLatLng())
                    .title("${store.name} (${store.getRemainStat()})")
                    .snippet(store.addr)
                    .icon(BitmapDescriptorFactory.defaultMarker(store.getColor()))
                markers.add(
                    googleMap.addMarker(markerOptions)
                )
            }
        }

        viewModel.progressVisibility.observe(this) { visible ->
            progressBar.visibility = when (visible) {
                true -> ProgressBar.VISIBLE
                false -> ProgressBar.GONE
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        val map = map?.also { googleMap = it } ?: return
        map.setOnCameraIdleListener(this)
        val westernDom = LatLng(37.6552462, 126.7701043)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(westernDom, DEFAULT_ZOOM_FACTOR))
    }

    override fun onCameraIdle() {
        val bound = googleMap.projection.visibleRegion.latLngBounds
        viewModel.setCurrentLocation(bound.center)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    getCurrentLocation()
                } else {
                    // TODO: Snackbar
                }
            }
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProvider.lastLocation
            .addOnSuccessListener { location ->
                Log.d(TAG, "Current location: ${location.latitude}, ${location.longitude}")
                val latLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_FACTOR))
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to get current location.", exception)
                // TODO: Snackbar
            }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        private const val LOCATION_REQ_CODE = 0x0100

        private const val DEFAULT_ZOOM_FACTOR = 15f
    }
}
