package com.example.gestionpersonal.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiDog {
    //URL API
    private const val BASE_URL = "https://dog.ceo/api/"

    val dogApiService: DogApiService by lazy {
        Retrofit.Builder() //Crear cliente
            .baseUrl(BASE_URL) //Asigna la url
            .addConverterFactory(GsonConverterFactory.create()) //GSON a JSON
            .build() //Construye la instancia
            .create(DogApiService::class.java)
    }
}
