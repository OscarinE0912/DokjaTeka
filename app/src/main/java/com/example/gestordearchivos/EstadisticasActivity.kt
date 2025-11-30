package com.example.gestordearchivos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.LibroDbHelper

class EstadisticasActivity : AppCompatActivity() {

    private lateinit var textViewEstadisticas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        textViewEstadisticas = findViewById(R.id.textViewEstadisticas)

        val dbHelper = LibroDbHelper(this)
        val listaLibros = dbHelper.obtenerLibros()

        val totalLibros = listaLibros.size

        val conteoPorGenero = listaLibros.groupingBy { it.genero }.eachCount()

        val builder = StringBuilder()
        builder.append("📚 Total de libros en tu biblioteca: $totalLibros\n\n")

        conteoPorGenero.forEach { (genero, cantidad) ->
            if (cantidad > 0) {
                builder.append("• $genero: $cantidad\n")
            }
        }

        textViewEstadisticas.text = builder.toString().trim()
    }
}
