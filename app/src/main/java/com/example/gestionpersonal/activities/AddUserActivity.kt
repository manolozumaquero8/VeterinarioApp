package com.example.gestionpersonal.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpersonal.R
import com.example.gestionpersonal.database.DatabaseHelper
import com.example.gestionpersonal.databinding.ActivityAddUserBinding
import com.example.gestionpersonal.models.User

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Asignamos el layout a la actividad
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la base de datos
        dbHelper = DatabaseHelper(this)

        // Recuperamos el ID del usuario y verificamos si es nuevo o existente
        userId = intent.getIntExtra("USER_ID", -1)

        if (userId != -1) {
            loadUserData(userId)
        }

        // Configuración del SeekBar Edad
        binding.seekBarAge.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvAgeLabel.text = "Edad: $progress años"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.tvAgeLabel.text = "Edad: ${binding.seekBarAge.progress} años"

        // Configuración del Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.dog_sizes_array,  // Valores del spinner en strings.xml (Grande, Mediano y Pequeño)
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSize.adapter = adapter

        // Botón Guardar
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val breed = binding.etBreed.text.toString()
            val age = binding.seekBarAge.progress
            val isVaccinated = binding.cbVaccinated.isChecked
            val size = binding.spinnerSize.selectedItem.toString()

            // Verificacion de campos
            if (name.isEmpty() || breed.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardamos o actualizamos
            if (userId == -1) {
                // Nuevo usuario
                val user = User(0, name, breed, age, isVaccinated, size)
                if (dbHelper.insertUser(user)) {
                    Toast.makeText(this, "Usuario guardado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Actualización usuario
                val updatedUser = User(userId, name, breed, age, isVaccinated, size)
                if (dbHelper.updateUser(updatedUser)) {
                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Cargar datos de un usuario existente
    private fun loadUserData(userId: Int) {
        val user = dbHelper.getUserById(userId)
        if (user != null) {
            binding.etName.setText(user.name)
            binding.etBreed.setText(user.breed)
            binding.seekBarAge.progress = user.age
            binding.cbVaccinated.isChecked = user.isVaccinated

            // Ajustamos el spinner
            val sizeAdapter = resources.getStringArray(R.array.dog_sizes_array)
            val sizePosition = sizeAdapter.indexOf(user.size)
            if (sizePosition >= 0) {
                binding.spinnerSize.setSelection(sizePosition)
            }
        }
    }
}
