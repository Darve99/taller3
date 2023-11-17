 package com.example.taller3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
 class HomeActivity : AppCompatActivity() {

     private val usersList = mutableListOf<String>()
     private lateinit var databaseReference: DatabaseReference
     private lateinit var currentUserID: String

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_home)

         val lista = findViewById<ListView>(R.id.lista)

         val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList)
         lista.adapter = adapter

         // Referencia a la base de datos de Firebase
         databaseReference = FirebaseDatabase.getInstance().reference.child("users")

         // Obtener el ID del usuario actual
         val currentUser = FirebaseAuth.getInstance().currentUser
         currentUserID = currentUser?.uid ?: ""

         loadUsers(adapter)
     }

     private fun loadUsers(adapter: ArrayAdapter<String>) {
         val query = databaseReference.orderByChild("Estado").equalTo("Disponible")
         query.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 usersList.clear()
                 for (data in snapshot.children) {
                     val userId = data.key // Obtener el ID del usuario de la base de datos
                     // Omitir al usuario actual en la lista
                     if (userId != currentUserID) {
                         val userName = data.child("firstName").getValue(String::class.java)
                         userName?.let {
                             usersList.add(it)
                         }
                     }
                 }
                 adapter.notifyDataSetChanged()
             }

             override fun onCancelled(error: DatabaseError) {
                 // Manejar errores al obtener datos
             }
         })
     }
 }
