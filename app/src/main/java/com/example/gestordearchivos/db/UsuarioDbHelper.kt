package com.example.gestordearchivos.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuarioDbHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                correo TEXT UNIQUE,
                contraseña TEXT,
                edad INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun registrarUsuario(correo: String, contraseña: String, edad: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("correo", correo)
            put("contraseña", contraseña)
            put("edad", edad)
        }
        return db.insert("usuarios", null, values) != -1L
    }

    fun validarUsuario(correo: String, contraseña: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?",
            arrayOf(correo, contraseña)
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun obtenerEdadUsuario(correo: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT edad FROM usuarios WHERE correo = ?",
            arrayOf(correo)
        )
        var edad = 0
        if (cursor.moveToFirst()) {
            edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad"))
        }
        cursor.close()
        return edad
    }
}
