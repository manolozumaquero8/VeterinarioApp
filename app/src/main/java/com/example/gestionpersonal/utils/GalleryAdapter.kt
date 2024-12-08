package com.example.gestionpersonal.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionpersonal.R

class GalleryAdapter(
    private val context: Context,
    private val images: List<Int>
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    //ViewHolder para mostrar las imagenes
    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Obtener imagen
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
    //Crear ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gallery_image, parent, false)
        return GalleryViewHolder(view)
    }
    //Asignar imagen
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }
    //Tamaño de la lista
    override fun getItemCount(): Int = images.size
}
