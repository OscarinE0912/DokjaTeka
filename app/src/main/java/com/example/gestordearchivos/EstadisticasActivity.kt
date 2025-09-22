package com.example.gestordearchivos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.LibroDbHelper

class EstadisticasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        val tvTotalLibros = findViewById<TextView>(R.id.tvTotalLibros)

        val db = LibroDbHelper(this).readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM libros", null)
        if (cursor.moveToFirst()) {
            val total = cursor.getInt(0)
            tvTotalLibros.text = "Total de libros: $total"
        }
        cursor.close()
    }
}
