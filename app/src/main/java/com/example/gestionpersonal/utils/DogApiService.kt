package com.example.gestionpersonal.utils

import com.example.gestionpersonal.models.DogResponse
import retrofit2.Call
import retrofit2.http.GET

interface DogApiService {
    //Metodo para obtener una imagen aleatoria de un perro
    @GET("breeds/image/random")
    fun getRandomDogImage(): Call<DogResponse>
}
