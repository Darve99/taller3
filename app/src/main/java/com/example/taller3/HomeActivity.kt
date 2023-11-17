 package com.example.taller3



import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

 class HomeActivity : AppCompatActivity() {

     private lateinit var auth: FirebaseAuth
     private lateinit var database: FirebaseDatabase
     private lateinit var userId: String
     private lateinit var userReference: DatabaseReference

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_home)

         auth = FirebaseAuth.getInstance()
         database = FirebaseDatabase.getInstance()

         val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
         setSupportActionBar(toolbar)

         val currentUser = auth.currentUser
         userId = currentUser?.uid ?: ""
         userReference = database.reference.child("users").child(userId)

         getUserStatus()
     }

     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu_home, menu)
         return true
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         return when (item.itemId) {
             R.id.action_change_status -> {
                 userReference.child("Estado").addListenerForSingleValueEvent(object : ValueEventListener {
                     override fun onDataChange(snapshot: DataSnapshot) {
                         val currentState = snapshot.getValue(String::class.java)

                         val newStatus = if (currentState == "Disponible") "No disponible" else "Disponible"

                         userReference.child("Estado").setValue(newStatus)
                             .addOnSuccessListener {
                                 Toast.makeText(this@HomeActivity, "Estado cambiado a $newStatus", Toast.LENGTH_SHORT).show()
                             }
                             .addOnFailureListener {
                                 Toast.makeText(this@HomeActivity, "Error al cambiar el estado", Toast.LENGTH_SHORT).show()
                             }
                     }

                     override fun onCancelled(error: DatabaseError) {
                         Toast.makeText(this@HomeActivity, "Error al obtener el estado", Toast.LENGTH_SHORT).show()
                     }
                 })
                 true
             }
             else -> super.onOptionsItemSelected(item)
         }
     }

     private fun getUserStatus() {
         userReference.child("Estado").addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 // No necesitas implementar nada aquí si la lógica se maneja en onOptionsItemSelected
             }

             override fun onCancelled(error: DatabaseError) {
                 Toast.makeText(this@HomeActivity, "Error al obtener el estado", Toast.LENGTH_SHORT).show()
             }
         })
     }
 }
