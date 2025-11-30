package com.example.gestordearchivos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.adapter.LibroAdapter
import com.example.gestordearchivos.db.LibroDbHelper
import com.example.gestordearchivos.model.Libro

class BibliotecaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibroAdapter
    private lateinit var dbHelper: LibroDbHelper
    private lateinit var textViewEmpty: TextView

    private var listaLibros: MutableList<Libro> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblioteca)

        dbHelper = LibroDbHelper(this)

        recyclerView = findViewById(R.id.recyclerViewLibros)
        recyclerView.layoutManager = LinearLayoutManager(this)

        textViewEmpty = findViewById(R.id.textViewEmpty)

        cargarLibros()
    }

    override fun onResume() {
        super.onResume()
        cargarLibros()
    }

    private fun obtenerEdadUsuario(): Int {
        val prefs = getSharedPreferences("usuario", MODE_PRIVATE)
        return prefs.getInt("edad", 0)
    }

    private fun cargarLibros() {
        val edadUsuario = obtenerEdadUsuario()
        val todos = dbHelper.obtenerLibros()

        listaLibros = if (edadUsuario < 18) {
            todos.filter { !it.soloAdultos }.toMutableList()
        } else {
            todos.toMutableList()
        }

        if (listaLibros.isEmpty()) {
            // ✅ Mostrar mensaje vacío
            textViewEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            // ✅ Mostrar lista
            textViewEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            adapter = LibroAdapter(listaLibros) { libro ->
                val intent = Intent(this, DetalleLibroActivity::class.java)
                intent.putExtra("LIBRO_SELECCIONADO", libro)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }
    }
}
