package com.example.gestionpersonal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionpersonal.R
import com.example.gestionpersonal.utils.GalleryAdapter

class GalleryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        //Obtener RecylcerView
        val galleryRecyclerView = view.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        val imageResources = listOf(
            //Recursos imagenes
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
        )
        //Configura el Reclycler para mostrar las imagenes en el cuadro
        galleryRecyclerView.layoutManager = GridLayoutManager(context, 2)
        //Configura el adaptador con la lista imagenes
        galleryRecyclerView.adapter = GalleryAdapter(requireContext(), imageResources)

        return view
    }
}
