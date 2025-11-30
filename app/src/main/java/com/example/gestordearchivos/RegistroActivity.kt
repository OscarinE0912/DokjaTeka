package com.example.gestordearchivos

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
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
            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Calcular edad
            val nacimiento = Calendar.getInstance()
            nacimiento.set(dpFecha.year, dpFecha.month, dpFecha.dayOfMonth)
            val hoy = Calendar.getInstance()
            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
            if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
                edad--
            }
            if (edad < 0) {
                Toast.makeText(this, "La fecha de nacimiento no es válida.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Registrar en BD
            val db = UsuarioDbHelper(this)
            val exito = db.registrarUsuario(correo, contraseña, edad)
            if (exito) {
                // ✅ Guardar edad en SharedPreferences para BibliotecaActivity
                val prefs = getSharedPreferences("usuario", MODE_PRIVATE)
                prefs.edit()
                    .putInt("edad", edad)
                    .putString("correo", correo)
                    .apply()

                Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Correo ya registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
