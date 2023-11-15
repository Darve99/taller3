package com.example.taller3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.taller3.databinding.ActivityMapasBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject

class MapasActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapasBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitud = 0.0
    var longitud = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MapasActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MapasActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

            }
            ActivityCompat.requestPermissions(
                this@MapasActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else if (ActivityCompat.checkSelfPermission(
                this@MapasActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapasActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocation()
                }
                return
            }

            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocation()
                }
                return
            }

            else -> {
                //algo mas
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this@MapasActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapasActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                Log.i("LOCATION", "onSucess location")
                if (location != null) {
                    latitud = location.latitude
                    longitud = location.longitude
                    Log.i("miLatitud", latitud.toString())
                    Log.i("miLongitud", longitud.toString())
                    //Poner un marcador en la ubicación del usuario
                    var miUbicacion = LatLng(latitud, longitud)
                    Log.i("Mi ubicación", miUbicacion.toString())
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                    mMap.addMarker(MarkerOptions().position(miUbicacion).title("Mi ubicación"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
                }
            }
        } else {
            requestPermission()
        }

        // Cargar el JSON desde el archivo de recursos
        val inputStream = resources.openRawResource(R.raw.locations)
        val jsonString = inputStream.bufferedReader().use { it.readText() }


        val jsonObject = JSONObject(jsonString)
        val locationsArray = jsonObject.getJSONArray("locationsArray")


        for (i in 0 until locationsArray.length()) {
            val location = locationsArray.getJSONObject(i)
            val lat = location.getDouble("latitude")
            val lng = location.getDouble("longitude")
            val name = location.getString("name")

            val latLng = LatLng(lat, lng)
            mMap.addMarker(MarkerOptions().position(latLng).title(name))
        }


        mMap.uiSettings.isZoomControlsEnabled = true // Habilitar los "gestures" como "pinch to zoom"
        mMap.uiSettings.isZoomGesturesEnabled = true // Habilitar los botones de zoom
    }

    private fun setLocation() {
        //setear la localización
        if (ActivityCompat.checkSelfPermission(
                this@MapasActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapasActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                Log.i("LOCATION", "onSucess location")
                if (location != null) {
                    latitud = location.latitude
                    longitud = location.longitude
                    Log.i("miLatitud", latitud.toString())
                    Log.i("miLongitud", longitud.toString())
                }
            }
        }
    }
}