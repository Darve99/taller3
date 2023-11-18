 package com.example.taller3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
 class HomeActivity : AppCompatActivity() {

     private lateinit var listView: ListView
     private lateinit var databaseReference: DatabaseReference
     private lateinit var usersList: MutableList<Pair<String, String>>

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_home)

         listView = findViewById(R.id.lista)
         usersList = mutableListOf()
         databaseReference = FirebaseDatabase.getInstance().reference.child("users")

         val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
         listView.adapter = adapter

         listView.setOnItemClickListener { _, _, position, _ ->
             val selectedUser = usersList[position]
             val userId = selectedUser.first

             databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onDataChange(snapshot: DataSnapshot) {
                     val latitude = snapshot.child("latitude").getValue(Double::class.java)
                     val longitude = snapshot.child("longitude").getValue(Double::class.java)

                     val intent = Intent(this@HomeActivity, CuatroActivity::class.java)
                     intent.putExtra("LATITUDE", latitude)
                     intent.putExtra("LONGITUDE", longitude)
                     startActivity(intent)
                 }

                 override fun onCancelled(error: DatabaseError) {
                     // Manejar errores al obtener datos
                 }
             })
         }

         loadUsers(adapter)
     }

     private fun loadUsers(adapter: ArrayAdapter<String>) {
         val user = FirebaseAuth.getInstance().currentUser
         val currentUserId = user?.uid ?: ""

         databaseReference.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 usersList.clear()
                 adapter.clear()

                 for (userSnapshot in snapshot.children) {
                     val userId = userSnapshot.key ?: ""
                     val userName = userSnapshot.child("firstName").getValue(String::class.java) ?: ""

                     if (userId != currentUserId) {
                         usersList.add(Pair(userId, userName))
                         adapter.add(userName)
                     }
                 }

                 adapter.notifyDataSetChanged()
             }

             override fun onCancelled(error: DatabaseError) {
                 // Manejar errores al cargar usuarios
             }
         })
     }
 }
