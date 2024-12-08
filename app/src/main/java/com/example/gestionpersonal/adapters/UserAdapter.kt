package com.example.gestionpersonal.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionpersonal.R
import com.example.gestionpersonal.activities.AddUserActivity
import com.example.gestionpersonal.database.DatabaseHelper
import com.example.gestionpersonal.models.User

class UserAdapter(
    private val context: Context,
    private var userList: List<User>,
    private val dbHelper: DatabaseHelper,
    private val onUserListUpdated: () -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    //ViewHolder para mostrar los datos de los usuarios que hemos guardado
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }
    //Crea la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }
    //Muestra los datos
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvName.text = user.name
        holder.tvDetails.text = "Raza: ${user.breed}, Edad: ${user.age}, Vacunado: ${if (user.isVaccinated) "Sí" else "No"}, Tamaño: ${user.size}"
        //Boton editar
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, AddUserActivity::class.java) //Navega a la pantalla de añadir usuario
            intent.putExtra("USER_ID", user.id) //Pasa el ID del usuario
            context.startActivity(intent) //Inicia la actividad
        }
        //Boton eliminar
        holder.btnDelete.setOnClickListener {
            if (dbHelper.deleteUser(user.id)) { //Eliminar usuario
                onUserListUpdated() //Actualiza la lista
            }
        }
    }

    override fun getItemCount(): Int = userList.size
    //Actualiza la lista
    fun updateList(newList: List<User>) {
        userList = newList //Cambiar la lista actual por la nueva
        notifyDataSetChanged()
    }
}
