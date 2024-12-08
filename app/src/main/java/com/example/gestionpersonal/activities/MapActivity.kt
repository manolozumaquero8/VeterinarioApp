package com.example.gestionpersonal.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gestionpersonal.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap //Mapa Google
    private lateinit var fusedLocationClient: FusedLocationProviderClient //Ubicacion usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        //Inicializar el mapa y el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //Configurar el mapa y lo llama
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fm_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //Boton para aumentar zoom
        findViewById<ImageButton>(R.id.masZoomButton).setOnClickListener {
            googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
        //Boton para disminuir zoom
        findViewById<ImageButton>(R.id.menosZoomButton).setOnClickListener {
            googleMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
        //Boton para mostrar ubicacion actual
        findViewById<ImageButton>(R.id.ubicacionActualButton).setOnClickListener {
            mostrarUbicacionActual()
        }
    }
    //LLama al mapa cuando esta listo
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        //Habilitar ubicacion si tiene los permisos
        enableMyLocation()
        //Ubicacion inicial con coordenadas en Almeria
        val initialLocation = LatLng(36.8417, -2.4650)
        googleMap.addMarker(MarkerOptions().position(initialLocation).title("Ubicación inicial"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 12f))
        //Poner un marcador al hacer click en el mapa
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear() //Quita el marcador actual
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng) //Pone el marcador en la posicion clicada
                    .title("Punto de interés")
            )
        }
    }
    //Habilita la ubicacion si tiene los permisos
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true //Activa la ubicacion
        } else {
            //Solicia permisos de ubicacion
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    //Ubicacion actual
    private fun mostrarUbicacionActual() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Ubicacion Actual"))
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                //Solicita los permisos
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    //Resultados de los permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }
}
