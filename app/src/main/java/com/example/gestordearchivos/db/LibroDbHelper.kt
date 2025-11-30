package com.example.gestordearchivos.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gestordearchivos.model.Libro

class LibroDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "libros.db"
        const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                autor TEXT,
                genero TEXT,
                sinopsis TEXT,
                rutaPdf TEXT,
                rutaPortada TEXT,
                soloAdultos INTEGER,
                progreso INTEGER DEFAULT 0
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS libros")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS libros")
        onCreate(db)
    }

    fun insertarLibro(libro: Libro): Long {
        val values = ContentValues().apply {
            put("titulo", libro.titulo)
            put("autor", libro.autor)
            put("genero", libro.genero)
            put("sinopsis", libro.sinopsis)
            put("rutaPdf", libro.rutaPdf)
            put("rutaPortada", libro.rutaPortada)
            put("soloAdultos", if (libro.soloAdultos) 1 else 0)
            put("progreso", libro.progreso)
        }
        return writableDatabase.insert("libros", null, values)
    }

    fun obtenerLibros(): List<Libro> {
        val lista = mutableListOf<Libro>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM libros", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Libro(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                        autor = cursor.getString(cursor.getColumnIndexOrThrow("autor")),
                        genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                        sinopsis = cursor.getString(cursor.getColumnIndexOrThrow("sinopsis")),
                        rutaPdf = cursor.getString(cursor.getColumnIndexOrThrow("rutaPdf")),
                        rutaPortada = cursor.getString(cursor.getColumnIndexOrThrow("rutaPortada")),
                        soloAdultos = cursor.getInt(cursor.getColumnIndexOrThrow("soloAdultos")) == 1,
                        progreso = cursor.getInt(cursor.getColumnIndexOrThrow("progreso"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun eliminarLibro(id: Int): Int {
        return writableDatabase.delete("libros", "id=?", arrayOf(id.toString()))
    }

    fun actualizarProgreso(id: Int, progreso: Int) {
        val values = ContentValues().apply {
            put("progreso", progreso)
        }
        writableDatabase.update("libros", values, "id=?", arrayOf(id.toString()))
    }
}
