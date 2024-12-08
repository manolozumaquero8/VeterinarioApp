package com.example.gestionpersonal.models

data class User(
    val id: Int, //ID
    val name: String, //Nombre
    val breed: String, //Raza
    val age: Int, //Edad
    val isVaccinated: Boolean, //Vacunado si o no
    val size: String //Tamaño
)
