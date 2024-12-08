package com.example.gestionpersonal.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpersonal.R
import com.example.gestionpersonal.utils.ApiDog
import com.example.gestionpersonal.models.DogResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogGalleryActivity : AppCompatActivity() {

    private lateinit var dogImageView: ImageView //Mostrar imagen perro
    private lateinit var fetchDogButton: Button //Cargar nuevo perro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_gallery)
        //Inicializar variables y asignar listeners
        dogImageView = findViewById(R.id.dogImageView)
        fetchDogButton = findViewById(R.id.fetchDogButton)

        fetchDogButton.setOnClickListener {
            fetchRandomDogImage()
        }
    }
    //Cargar imagen perro aleatoria
    private fun fetchRandomDogImage() {
        //Llama al ApiDog y comprueba (si es correcto) manda una URL de la imagen
        ApiDog.dogApiService.getRandomDogImage().enqueue(object : Callback<DogResponse> {
            override fun onResponse(call: Call<DogResponse>, response: Response<DogResponse>) {
                if (response.isSuccessful) {
                    val dogImageUrl = response.body()?.message
                    dogImageUrl?.let {
                        //Cargar imagen perro
                        Picasso.get()
                            .load(it) //URL
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .into(dogImageView)
                    }
                } else {
                    //Sino mostrara atraves de un Toast un mensaje de error
                    Toast.makeText(this@DogGalleryActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DogResponse>, t: Throwable) {
                //Mostrara un toast diciendo que no se puedo cargar la Api
                Toast.makeText(this@DogGalleryActivity, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
