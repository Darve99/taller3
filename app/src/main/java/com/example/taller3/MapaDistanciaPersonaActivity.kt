package com.example.taller3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.taller3.databinding.ActivityMapaDistanciaPersonaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class MapaDistanciaPersonaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaDistanciaPersonaBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitud = 0.0
    var longitud = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaDistanciaPersonaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this@MapaDistanciaPersonaActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapaDistanciaPersonaActivity,
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

                    //Recibir el intent de la actividad anterior
                    val intent = intent
                    val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
                    val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
                    val ubicacionPersona = LatLng(latitude, longitude)
                    //añadir marcador de la ubicación de la persona
                    mMap.addMarker(
                        MarkerOptions().position(ubicacionPersona).title("Ubicación de la persona")
                    )

                    val distancia = FloatArray(1)
                    Location.distanceBetween(
                        miUbicacion.latitude,
                        miUbicacion.longitude,
                        ubicacionPersona.latitude,
                        ubicacionPersona.longitude,
                        distancia
                    )

                    val polylineOptions = PolylineOptions()
                        .add(miUbicacion, ubicacionPersona)
                        .width(5f)
                        .color(Color.RED)

                    val polyline: Polyline = mMap.addPolyline(polylineOptions)

                    //hacer un toast con la distancia
                    Toast.makeText(
                        this@MapaDistanciaPersonaActivity,
                        "La distancia es: " + distancia[0].toString() + " metros",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }

        mMap.uiSettings.isZoomControlsEnabled = true // Habilitar los "gestures" como "pinch to zoom"
        mMap.uiSettings.isZoomGesturesEnabled = true // Habilitar los botones de zoom
    }
}