package com.example.gestionpersonal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gestionpersonal.R

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Asociar el fragment con el layout
        val view = inflater.inflate(R.layout.fragment_map, container, false)
       //Llamar al mapa
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }
    //Metodo para mostrar el mapa
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        try {
            //Ubicacion actual
            val location = LatLng(36.8417, -2.4650)
            //Marcador en almeria
            googleMap.addMarker(MarkerOptions().position(location).title("Almería"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        } catch (e: Exception) {
            e.printStackTrace() //Lanza excepcion
        }
    }

}
