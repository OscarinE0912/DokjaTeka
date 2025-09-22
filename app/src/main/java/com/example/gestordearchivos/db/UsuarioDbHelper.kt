package com.example.gestordearchivos.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuarioDbHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                correo TEXT UNIQUE,
                contraseña TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun registrarUsuario(correo: String, contraseña: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("correo", correo)
            put("contraseña", contraseña)
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
}
