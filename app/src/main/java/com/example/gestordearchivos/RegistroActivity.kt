package com.example.gestordearchivos

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestordearchivos.db.UsuarioDbHelper
import java.util.Calendar

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContraseña = findViewById<EditText>(R.id.etContraseña)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val dpFecha = findViewById<DatePicker>(R.id.dpFecha)

        btnRegistrar.setOnClickListener {
            val correo = etCorreo.text.toString()
            val contraseña = etContraseña.text.toString()
            val nacimiento = Calendar.getInstance()
            val hoy = Calendar.getInstance()

            nacimiento.set(dpFecha.year,dpFecha.month,dpFecha.dayOfMonth)
            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
            if (edad < 18){
                Toast.makeText(this,"Eres demasiado menor para usar esta app", Toast.LENGTH_LONG).show()
            }else{
                
                if (correo.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val db = UsuarioDbHelper(this)
                val exito = db.registrarUsuario(correo, contraseña)
    
                if (exito) {
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Correo ya registrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
