package com.example.gestionpersonal.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gestionpersonal.models.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 3
        //Constantes
        private const val TABLE_USERS = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BREED = "breed"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_VACCINATED = "vaccinated"
        private const val COLUMN_SIZE = "size"
    }
    //Creacion de la tabla
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_BREED TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_VACCINATED INTEGER,
                $COLUMN_SIZE TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }
    //Actualizacion de la tabla
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS") //Eliminar la tabla anterior
        onCreate(db) //Crear la tabla nueva
    }
    //Añadir usuario
    fun insertUser(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_BREED, user.breed)
            put(COLUMN_AGE, user.age)
            put(COLUMN_VACCINATED, if (user.isVaccinated) 1 else 0)
            put(COLUMN_SIZE, user.size)
        }
        return db.insert(TABLE_USERS, null, values) != -1L
    }
    //Obtener todos los usuarios
    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val breed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BREED))
                val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                val vaccinated = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VACCINATED)) == 1
                val size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
                users.add(User(id, name, breed, age, vaccinated, size))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }
    //Atraves de ID obtener al usuario
    fun getUserById(userId: Int): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val breed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BREED))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
            val vaccinated = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VACCINATED)) == 1
            val size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
            User(id, name, breed, age, vaccinated, size)
        } else {
            null
        }.also {
            cursor.close()
        }
    }
    //Actualizxa la informacion
    fun updateUser(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_BREED, user.breed)
            put(COLUMN_AGE, user.age)
            put(COLUMN_VACCINATED, if (user.isVaccinated) 1 else 0)
            put(COLUMN_SIZE, user.size)
        }
        val result = db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(user.id.toString()))
        return result > 0
    }
    //Eliminar usuario
    fun deleteUser(userId: Int): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(userId.toString())) > 0
    }
    //Buscar usuarios por nombre o raza
    fun searchUsers(query: String?): List<User> {
        if (query.isNullOrEmpty()) return getAllUsers()

        val users = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_NAME LIKE ? OR $COLUMN_BREED LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val breed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BREED))
                val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                val vaccinated = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VACCINATED)) == 1
                val size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
                users.add(User(id, name, breed, age, vaccinated, size))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }
}
