package com.example.gestordearchivos

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.LibroDbHelper
import java.io.File
import java.io.FileOutputStream

class UploadActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etAutor: EditText
    private lateinit var btnSeleccionar: Button
    private lateinit var btnSubir: Button
    private var pdfUri: Uri? = null

    private val seleccionarPdfLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            pdfUri = uri
            Toast.makeText(this, "PDF seleccionado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        etTitulo = findViewById(R.id.etTitulo)
        etAutor = findViewById(R.id.etAutor)
        btnSeleccionar = findViewById(R.id.btnSeleccionar)
        btnSubir = findViewById(R.id.btnSubir)

        btnSeleccionar.setOnClickListener {
            seleccionarPdfLauncher.launch("application/pdf")
        }

        btnSubir.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val autor = etAutor.text.toString()

            if (titulo.isEmpty() || autor.isEmpty() || pdfUri == null) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inputStream = contentResolver.openInputStream(pdfUri!!)
            val file = File(filesDir, "libros")
            if (!file.exists()) file.mkdir()
            val destino = File(file, "${System.currentTimeMillis()}.pdf")
            val outputStream = FileOutputStream(destino)
            inputStream?.copyTo(outputStream)

            val db = LibroDbHelper(this).writableDatabase
            val values = ContentValues().apply {
                put("titulo", titulo)
                put("autor", autor)
                put("ruta", destino.absolutePath)
            }
            db.insert("libros", null, values)

            Toast.makeText(this, "Libro guardado localmente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
