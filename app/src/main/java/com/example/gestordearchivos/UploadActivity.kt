package com.example.gestordearchivos

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.LibroDbHelper
import com.example.gestordearchivos.model.Libro
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UploadActivity : AppCompatActivity() {

    private lateinit var editTextTitulo: EditText
    private lateinit var editTextAutor: EditText
    private lateinit var spinnerGenero: Spinner
    private lateinit var editTextSinopsis: EditText
    private lateinit var buttonSeleccionarArchivo: Button
    private lateinit var buttonSeleccionarPortada: Button
    private lateinit var buttonGuardar: Button
    private lateinit var imageViewPortadaPreview: ImageView
    private lateinit var checkBoxAdultos: CheckBox

    private var rutaPDF: String = ""
    private var rutaPortada: String = ""
    private lateinit var dbHelper: LibroDbHelper

    private val selectPdfLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val fileName = getFileName(uri)
                rutaPDF = saveFileToInternalStorage(uri, fileName) ?: ""
                buttonSeleccionarArchivo.text = "PDF Seleccionado: $fileName"
                Toast.makeText(this, "Ruta PDF: $rutaPDF", Toast.LENGTH_SHORT).show()
            }
        }

    private val selectImageLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val fileName = getFileName(uri)
                rutaPortada = saveFileToInternalStorage(uri, fileName) ?: ""
                buttonSeleccionarPortada.text = "Portada Seleccionada: $fileName"
                imageViewPortadaPreview.setImageURI(uri)
                imageViewPortadaPreview.visibility = View.VISIBLE
                Toast.makeText(this, "Ruta Portada: $rutaPortada", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        dbHelper = LibroDbHelper(this)

        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextAutor = findViewById(R.id.editTextAutor)
        spinnerGenero = findViewById(R.id.spinnerGenero)
        editTextSinopsis = findViewById(R.id.editTextSinopsis)
        buttonSeleccionarArchivo = findViewById(R.id.buttonSeleccionarArchivo)
        buttonSeleccionarPortada = findViewById(R.id.buttonSeleccionarPortada)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        imageViewPortadaPreview = findViewById(R.id.imageViewPortadaPreview)
        checkBoxAdultos = findViewById(R.id.checkBoxAdultos)

        setupSpinner()

        buttonSeleccionarArchivo.setOnClickListener {
            selectPdfLauncher.launch("application/pdf")
        }
        buttonSeleccionarPortada.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
        buttonGuardar.setOnClickListener {
            guardarLibro()
        }
    }

    private fun setupSpinner() {
        val generos = arrayOf("Ficción", "No Ficción", "Ciencia Ficción", "Fantasía", "Thriller", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapter
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex)
                    }
                }
            }
        }
        return result ?: uri.lastPathSegment ?: "ArchivoDesconocido"
    }

    private fun saveFileToInternalStorage(uri: Uri, fileName: String): String? {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Toast.makeText(this, "Error al abrir archivo", Toast.LENGTH_SHORT).show()
                return null
            }
            val dir = File(filesDir, "uploads")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            return file.absolutePath
        } catch (e: Exception) {
            Log.e("UploadActivity", "Error al guardar archivo: ${e.message}")
            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
            return null
        }
    }

    private fun generateThumbnailFromPdf(pdfPath: String): String? {
        if (pdfPath.isEmpty()) return null
        val pdfFile = File(pdfPath)
        if (!pdfFile.exists()) return null
        try {
            val fileDescriptor = contentResolver.openFileDescriptor(Uri.fromFile(pdfFile), "r") ?: return null
            val renderer = PdfRenderer(fileDescriptor)
            val page = renderer.openPage(0)
            val desiredWidth = 300
            val ratio = page.height.toFloat() / page.width.toFloat()
            val desiredHeight = (desiredWidth * ratio).toInt()
            val bitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()
            fileDescriptor.close()
            val dir = File(filesDir, "thumbnails")
            if (!dir.exists()) dir.mkdirs()
            val thumbnailFileName = "thumb_" + pdfFile.nameWithoutExtension + ".jpg"
            val thumbnailFile = File(dir, thumbnailFileName)
            FileOutputStream(thumbnailFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }
            return thumbnailFile.absolutePath
        } catch (e: Exception) {
            Log.e("UploadActivity", "Error generando thumbnail: ${e.message}")
            return null
        }
    }

    private fun guardarLibro() {
        val titulo = editTextTitulo.text.toString().trim()
        val autor = editTextAutor.text.toString().trim()
        val generoSeleccionado = spinnerGenero.selectedItem.toString()
        val sinopsis = editTextSinopsis.text.toString().trim()
        val esAdulto = checkBoxAdultos.isChecked

        if (titulo.isEmpty() || autor.isEmpty() || rutaPDF.isEmpty()) {
            Toast.makeText(this, "Por favor, completa Título, Autor y selecciona el archivo PDF.", Toast.LENGTH_LONG).show()
            return
        }

        var portadaFinal = rutaPortada
        if (portadaFinal.isEmpty()) {
            val thumbnailPath = generateThumbnailFromPdf(rutaPDF)
            if (thumbnailPath != null) {
                portadaFinal = thumbnailPath
                Toast.makeText(this, "Portada generada desde PDF.", Toast.LENGTH_SHORT).show()
            }
        }

        val nuevoLibro = Libro(
            titulo = titulo,
            autor = autor,
            genero = generoSeleccionado,
            sinopsis = sinopsis,
            rutaPdf = rutaPDF,
            rutaPortada = portadaFinal,
            soloAdultos = esAdulto
        )

        val id = dbHelper.insertarLibro(nuevoLibro)
        if (id > 0) {
            Toast.makeText(this, "Libro guardado con éxito! ID: $id", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el libro en la base de datos.", Toast.LENGTH_LONG).show()
        }
    }
}
