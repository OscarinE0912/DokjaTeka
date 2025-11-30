package com.example.gestordearchivos

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.LibroDbHelper
import com.example.gestordearchivos.model.Libro
import java.io.File

class DetalleLibroActivity : AppCompatActivity() {

    private lateinit var imgPortada: ImageView
    private lateinit var etTitulo: EditText
    private lateinit var etAutor: EditText
    private lateinit var tvGenero: TextView
    private lateinit var tvProgreso: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnGuardar: Button
    private lateinit var btnVerPdf: Button
    private lateinit var btnEliminar: Button

    private lateinit var dbHelper: LibroDbHelper
    private lateinit var libro: Libro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_libro)

        // Referencias UI
        imgPortada = findViewById(R.id.imgDetallePortada)
        etTitulo = findViewById(R.id.etDetalleTitulo)
        etAutor = findViewById(R.id.etDetalleAutor)
        tvGenero = findViewById(R.id.tvDetalleGenero)
        tvProgreso = findViewById(R.id.tvProgreso)
        progressBar = findViewById(R.id.progressBarDetalle)
        btnGuardar = findViewById(R.id.btnDetalleGuardar)
        btnVerPdf = findViewById(R.id.btnDetalleVerPdf)
        btnEliminar = findViewById(R.id.btnDetalleEliminar)

        dbHelper = LibroDbHelper(this)

        // ✅ Manejo seguro de Serializable según versión de Android
        libro = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("LIBRO_SELECCIONADO", Libro::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("LIBRO_SELECCIONADO") as Libro
        }

        mostrarDatos()
        configurarBotones()
    }

    override fun onResume() {
        super.onResume()
        val libroActualizado = dbHelper.obtenerLibros().find { it.id == libro.id }
        if (libroActualizado != null) {
            tvProgreso.text = "Avance de lectura: ${libroActualizado.progreso}%"
            progressBar.progress = libroActualizado.progreso
        }
    }

    private fun mostrarDatos() {
        etTitulo.setText(libro.titulo)
        etAutor.setText(libro.autor)
        tvGenero.text = "Género: ${libro.genero}"
        tvProgreso.text = "Avance de lectura: ${libro.progreso}%"
        progressBar.progress = libro.progreso
    }

    private fun configurarBotones() {
        // Guardar cambios
        btnGuardar.setOnClickListener {
            val nuevoTitulo = etTitulo.text.toString()
            val nuevoAutor = etAutor.text.toString()

            val values = ContentValues().apply {
                put("titulo", nuevoTitulo)
                put("autor", nuevoAutor)
            }
            dbHelper.writableDatabase.update("libros", values, "id=?", arrayOf(libro.id.toString()))

            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
        }

        // Ver PDF
        btnVerPdf.setOnClickListener {
            val file = File(libro.rutaPdf)
            if (file.exists()) {
                val intent = Intent(this, VisorPdfActivity::class.java)
                intent.putExtra("LIBRO_SELECCIONADO", libro)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Archivo PDF no encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        // Eliminar libro
        btnEliminar.setOnClickListener {
            dbHelper.eliminarLibro(libro.id)
            Toast.makeText(this, "Libro eliminado", Toast.LENGTH_SHORT).show()
            finish() // volver a la biblioteca
        }
    }
}
