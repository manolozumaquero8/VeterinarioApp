package com.example.gestionpersonal.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gestionpersonal.R
import com.example.gestionpersonal.adapters.UserAdapter
import com.example.gestionpersonal.database.DatabaseHelper
import com.example.gestionpersonal.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        loadUsers()

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText)
                return true
            }
        })

        binding.fabAddUser.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }


    private fun loadUsers() {
        val users = dbHelper.getAllUsers()
        userAdapter = UserAdapter(this, users, dbHelper) {
            loadUsers()
        }
        binding.recyclerViewUsers.adapter = userAdapter
    }

    private fun filterUsers(query: String?) {
        val filteredUsers = dbHelper.searchUsers(query)
        userAdapter.updateList(filteredUsers)
    }
}
