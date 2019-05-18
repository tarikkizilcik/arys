package org.example.arys.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DatabaseRef {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val adsRef: DatabaseReference = database.getReference("ads")
}