package com.example.gestionpersonal.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpersonal.R
import com.example.gestionpersonal.database.DatabaseHelper
import com.example.gestionpersonal.databinding.ActivityEditUserBinding
import com.example.gestionpersonal.models.User

class EditUserActivity : AppCompatActivity() {
    //Cargamos los datos iguel que en AddUserActivity
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        userId = intent.getIntExtra("USER_ID", -1)

        if (userId != -1) {
            loadUserData(userId)
        } else {
            Toast.makeText(this, "Error al cargar el usuario", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val breed = binding.etBreed.text.toString()
            val age = binding.etAge.text.toString().toIntOrNull() ?: 0
            val isVaccinated = binding.cbVaccinated.isChecked
            val size = binding.spinnerSize.selectedItem.toString()
            //Validar si estan vacios los camos
            if (name.isNotEmpty() && breed.isNotEmpty()) {
                //Crear el objeto usuario actualizado
                val updatedUser = User(userId, name, breed, age, isVaccinated, size)
                if (dbHelper.updateUser(updatedUser)) {
                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                //Mostrar un mensaje de que hay campos vacios
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Cargar usuario existente base de datos
    private fun loadUserData(userId: Int) {
        val user = dbHelper.getUserById(userId) // Cargamos el usuario por ID
        if (user != null) {
            //Asignamos los datos
            binding.etName.setText(user.name)
            binding.etBreed.setText(user.breed)
            binding.etAge.setText(user.age.toString())
            binding.cbVaccinated.isChecked = user.isVaccinated
            //Configuracion spinner
            val sizeAdapter = resources.getStringArray(R.array.dog_sizes_array)
            val sizePosition = sizeAdapter.indexOf(user.size)
            if (sizePosition >= 0) {
                binding.spinnerSize.setSelection(sizePosition)
            }
        } else {
            //Toast que no se encontro el usuario
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
