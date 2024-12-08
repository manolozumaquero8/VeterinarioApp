package com.example.gestionpersonal.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpersonal.R

class GalleryActivity : AppCompatActivity() {

    private val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    )
    //Indice actual
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery) //Asignar diseño
        //Mostrar imagen y botones
        val ivGallery = findViewById<ImageView>(R.id.iv_gallery)
        val btnPrevious = findViewById<Button>(R.id.btn_previous)
        val btnNext = findViewById<Button>(R.id.btn_next)
        //Mostrar imagen inicial
        ivGallery.setImageResource(images[currentIndex])
        //Boton anterior
        btnPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                ivGallery.setImageResource(images[currentIndex])
            }
        }
        //Boton siguiente
        btnNext.setOnClickListener {
            if (currentIndex < images.size - 1) {
                currentIndex++
                ivGallery.setImageResource(images[currentIndex])
            }
        }
    }
}
