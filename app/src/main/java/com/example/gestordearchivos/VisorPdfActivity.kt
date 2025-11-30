package com.example.gestordearchivos

import android.os.Build
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.LibroDbHelper
import com.example.gestordearchivos.model.Libro
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class VisorPdfActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var dbHelper: LibroDbHelper
    private lateinit var libro: Libro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visor_pdf)

        pdfView = findViewById(R.id.pdfView)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        dbHelper = LibroDbHelper(this)

        libro = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("LIBRO_SELECCIONADO", Libro::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("LIBRO_SELECCIONADO") as Libro
        }

        val file = File(libro.rutaPdf)
        if (file.exists()) {
            val prefs = getSharedPreferences("usuario", MODE_PRIVATE)
            val ultimaPagina = prefs.getInt("pagina_${libro.id}", 0)

            pdfView.fromFile(file)
                .defaultPage(ultimaPagina)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageChange { page, pageCount ->
                    prefs.edit().putInt("pagina_${libro.id}", page).apply()

                    val porcentaje = ((page + 1).toFloat() / pageCount.toFloat()) * 100
                    progressBar.progress = porcentaje.toInt()
                    progressText.text = "Leído: ${porcentaje.toInt()}%"
                    dbHelper.actualizarProgreso(libro.id, porcentaje.toInt())
                }
                .load()
        } else {
            Toast.makeText(this, "Error: Archivo PDF no encontrado.", Toast.LENGTH_LONG).show()
        }
    }
}
