package com.example.gestordearchivos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val SMS_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBiblioteca = findViewById<Button>(R.id.btnBiblioteca)
        val btnSubir = findViewById<Button>(R.id.btnSubir)
        val btnEstadisticas = findViewById<Button>(R.id.btnEstadisticas)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnBiblioteca.setOnClickListener {
            startActivity(Intent(this, BibliotecaActivity::class.java))
        }

        btnSubir.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        btnEstadisticas.setOnClickListener {
            startActivity(Intent(this, EstadisticasActivity::class.java))
        }

        btnLogout.setOnClickListener {
            val prefsEdit = getSharedPreferences("usuario", MODE_PRIVATE).edit()
            prefsEdit.clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
