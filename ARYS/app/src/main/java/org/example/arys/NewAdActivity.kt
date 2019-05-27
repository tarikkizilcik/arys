package org.example.arys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_new_ad.*
import org.example.arys.data.Advertisement
import org.example.arys.database.DatabaseConnection

class NewAdActivity : AppCompatActivity() {
    companion object {
        const val TAG = "NewAdActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ad)

        buttonSave.setOnClickListener(onClickSaveButton)
        buttonCancel.setOnClickListener(onClickCancelButton)
    }

    private val onClickSaveButton = View.OnClickListener {
        val advertisement = Advertisement(
            editTextFirmName.text.toString(),
            editTextLatitude.text.toString().toDouble(),
            editTextLongitude.text.toString().toDouble(),
            editTextPromotionContent.text.toString(),
            editTextPromotionDuration.text.toString().toLong()
        )

        DatabaseConnection.addAd(advertisement, onSuccessListener, onFailureListener)

        finish()
    }

    private val onClickCancelButton = View.OnClickListener {
        finish()
    }

    private val onSuccessListener = OnSuccessListener<DocumentReference> {
        Log.i(TAG, "DocumentSnapshot added with ID: ${it.id}")
    }

    private val onFailureListener = OnFailureListener {
        Log.e(TAG, "Error adding document", it)
    }
}
