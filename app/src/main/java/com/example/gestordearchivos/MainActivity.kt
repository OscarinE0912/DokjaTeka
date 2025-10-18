package com.example.gestordearchivos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val SMS_PERMISSION_CODE=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBiblioteca = findViewById<Button>(R.id.btnBiblioteca)
        val btnSubir = findViewById<Button>(R.id.btnSubir)
        val btnEstadisticas = findViewById<Button>(R.id.btnEstadisticas)

        btnBiblioteca.setOnClickListener {
            startActivity(Intent(this, BibliotecaActivity::class.java))
        }

        btnSubir.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        btnEstadisticas.setOnClickListener {
            startActivity(Intent(this, EstadisticasActivity::class.java))
        }

        //para sms
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED  ){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                SMS_PERMISSION_CODE
            )
        }
    }
}
