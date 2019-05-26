package org.example.arys.database

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import org.example.arys.data.Advertisement

object DatabaseConnection {
    private lateinit var advertisements: CollectionReference

    fun addAd(
        advertisement: Advertisement,
        onSuccessListener: OnSuccessListener<DocumentReference>,
        onFailureListener: OnFailureListener
    ) {
        if (!::advertisements.isInitialized) {
            val db = FirebaseFirestore.getInstance()
            advertisements = db.collection("ads")
        }

        advertisements
            .add(advertisement)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }
}