package com.example.gestionpersonal

import android.app.Application
import com.google.firebase.FirebaseApp

class MainApp : Application() {
    //Inicializar Firebase
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}