package com.example.gestordearchivos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class VisorPdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visor_pdf)

        val ruta = intent.getStringExtra("ruta")
        val pdfView = findViewById<PDFView>(R.id.pdfView)

        if (ruta != null) {
            val archivo = File(ruta)
            if (archivo.exists()) {
                pdfView.fromFile(archivo)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .load()
            }
        }
    }
}
