package com.example.gestordearchivos

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.db.LibroDbHelper
import com.example.gestordearchivos.model.Libro

class BibliotecaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibroAdapter
    private val listaLibros = mutableListOf<Libro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblioteca)

        recyclerView = findViewById(R.id.recyclerLibros)
        adapter = LibroAdapter(listaLibros) { libro ->
            val intent = Intent(this, VisorPdfActivity::class.java)
            intent.putExtra("ruta", libro.ruta)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        cargarLibros()
    }

    private fun cargarLibros() {
        listaLibros.clear()
        val db = LibroDbHelper(this).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM libros", null)
        while (cursor.moveToNext()) {
            val libro = Libro(
                id = cursor.getInt(0),
                titulo = cursor.getString(1),
                autor = cursor.getString(2),
                ruta = cursor.getString(3)
            )
            listaLibros.add(libro)
        }
        cursor.close()
        adapter.notifyDataSetChanged()
    }
}
