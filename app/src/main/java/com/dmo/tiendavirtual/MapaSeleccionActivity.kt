package com.dmo.tiendavirtual

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapaSeleccionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val almeria = LatLng(36.8340, -2.4637) // Coordenadas de Almería
    private val defaultZoom = 8f // Nivel de zoom


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_seleccion)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Mover cámara a España por defecto
        val espana = LatLng(40.0, -3.7)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(almeria, defaultZoom))

        // Listener de toque en el mapa
        mMap.setOnMapClickListener { latLng ->
            val data = Intent()
            data.putExtra("lat", latLng.latitude)
            data.putExtra("lng", latLng.longitude)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
