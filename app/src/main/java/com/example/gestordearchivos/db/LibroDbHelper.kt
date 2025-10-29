package com.example.gestordearchivos.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

<<<<<<< HEAD
class LibroDbHelper(context: Context) : SQLiteOpenHelper(context, "libros.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                autor TEXT,
                ruta TEXT
            )
        """.trimIndent())
=======
class LibroDbHelper(context: Context) : SQLiteOpenHelper(context, "libros.db", null, 2){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
        CREATE TABLE libros (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT,
            autor TEXT,
            ruta TEXT,
            portada TEXT
        )
    """.trimIndent())
>>>>>>> 11ca62c (Primer commit del proyecto)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS libros")
        onCreate(db)
    }
}
