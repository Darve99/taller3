 package com.example.taller3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
 class HomeActivity : AppCompatActivity() {

     private val usersList = mutableListOf<String>()

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_home)

         val lista = findViewById<ListView>(R.id.lista)



         val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList)
         lista.adapter = adapter
     }

     private fun loadUsers(adapter: ArrayAdapter<String>) {

     }
 }
