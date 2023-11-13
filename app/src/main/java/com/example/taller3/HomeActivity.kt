package com.example.taller3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // Cerrar sesión
            signOut()
            // Redirigir a la pantalla de inicio de sesión o a donde prefieras
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun signOut() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

}
