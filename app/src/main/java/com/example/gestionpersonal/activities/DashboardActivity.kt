package com.example.gestionpersonal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.gestionpersonal.R
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Pantalla perfil usuario y la inicia
        findViewById<Button>(R.id.btn_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Pantalla de mapa y la inicia
        findViewById<Button>(R.id.btn_map).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        // Galería de imágenes de perros y la inicia
        findViewById<Button>(R.id.btn_api).setOnClickListener {
            startActivity(Intent(this, DogGalleryActivity::class.java))
        }

        // Galería de imágenes local y la inicia
        findViewById<Button>(R.id.btn_gallery).setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        // Regresar al login con la autentufucacion de firebase
        findViewById<Button>(R.id.btn_salir).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
