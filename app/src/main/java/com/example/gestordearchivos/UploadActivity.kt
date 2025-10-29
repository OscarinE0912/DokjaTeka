package com.example.gestordearchivos

import android.content.ContentValues
import android.content.Intent
<<<<<<< HEAD
import android.net.Uri
import android.os.Bundle
=======
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
>>>>>>> 11ca62c (Primer commit del proyecto)
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
<<<<<<< HEAD
    private var pdfUri: Uri? = null
=======
    private lateinit var imgPortada: ImageView
    private lateinit var btnSeleccionarImagen: Button

    private var pdfUri: Uri? = null
    private var imagenUri: Uri? = null
>>>>>>> 11ca62c (Primer commit del proyecto)

    private val seleccionarPdfLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            pdfUri = uri
            Toast.makeText(this, "PDF seleccionado", Toast.LENGTH_SHORT).show()
        }
    }

<<<<<<< HEAD
=======
    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagenUri = uri
            imgPortada.setImageURI(uri)
            Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

>>>>>>> 11ca62c (Primer commit del proyecto)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        etTitulo = findViewById(R.id.etTitulo)
        etAutor = findViewById(R.id.etAutor)
        btnSeleccionar = findViewById(R.id.btnSeleccionar)
        btnSubir = findViewById(R.id.btnSubir)
<<<<<<< HEAD
=======
        imgPortada = findViewById(R.id.imgPortada)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
>>>>>>> 11ca62c (Primer commit del proyecto)

        btnSeleccionar.setOnClickListener {
            seleccionarPdfLauncher.launch("application/pdf")
        }

<<<<<<< HEAD
=======
        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagenLauncher.launch("image/*")
        }

>>>>>>> 11ca62c (Primer commit del proyecto)
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

<<<<<<< HEAD
=======
            val portadaPath = if (imagenUri != null) {
                val inputStreamImg = contentResolver.openInputStream(imagenUri!!)
                val imgFile = File(filesDir, "portadas")
                if (!imgFile.exists()) imgFile.mkdir()
                val destinoImg = File(imgFile, "${System.currentTimeMillis()}.jpg")
                val outputStreamImg = FileOutputStream(destinoImg)
                inputStreamImg?.copyTo(outputStreamImg)
                destinoImg.absolutePath
            } else {
                generarMiniaturaDesdePdf(destino)?.absolutePath
            }

>>>>>>> 11ca62c (Primer commit del proyecto)
            val db = LibroDbHelper(this).writableDatabase
            val values = ContentValues().apply {
                put("titulo", titulo)
                put("autor", autor)
                put("ruta", destino.absolutePath)
<<<<<<< HEAD
=======
                put("portada", portadaPath)
>>>>>>> 11ca62c (Primer commit del proyecto)
            }
            db.insert("libros", null, values)

            Toast.makeText(this, "Libro guardado localmente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
<<<<<<< HEAD
=======

    private fun generarMiniaturaDesdePdf(pdfFile: File): File? {
        return try {
            val fileDescriptor =
                ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)
            val page = renderer.openPage(0)

            val width = 300 // Puedes ajustar el tamaño si lo deseas
            val height = (page.height * width) / page.width
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()

            val portadaDir = File(filesDir, "portadas")
            if (!portadaDir.exists()) portadaDir.mkdir()
            val portadaFile = File(portadaDir, "${System.currentTimeMillis()}_auto.jpg")
            val outputStream = FileOutputStream(portadaFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()

            portadaFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
>>>>>>> 11ca62c (Primer commit del proyecto)
}
