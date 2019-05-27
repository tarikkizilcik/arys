package org.example.arys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_new_ad.*
import org.example.arys.data.Advertisement
import org.example.arys.database.DatabaseConnection
import java.text.DecimalFormat

class NewAdActivity : AppCompatActivity() {
    companion object {
        const val TAG = "NewAdActivity"
        const val REQUEST_CODE_LAT_LNG = 0
    }

    private val decimalFormat = DecimalFormat("0.000")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ad)

        buttonSave.setOnClickListener(onClickSaveButton)
        buttonCancel.setOnClickListener(onClickCancelButton)

        imageButtonMarker.setOnClickListener(onClickImageButtonMarker)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return

        if (requestCode == REQUEST_CODE_LAT_LNG && resultCode == Activity.RESULT_OK) {
            val location = data.getParcelableExtra<LatLng>(MapsActivity.EXTRA_LAT_LNG)

            val latitude = decimalFormat.format(location.latitude)
            val longitude = decimalFormat.format(location.longitude)

            textViewLatitude.text = getString(R.string.text_with_colon, getString(R.string.latitude), latitude)
            textViewLongitude.text = getString(R.string.text_with_colon, getString(R.string.longitude), longitude)
        }
    }

    private val onClickSaveButton = View.OnClickListener {
        val location = LatLng(
            textViewLatitude.text.toString().toDouble(),
            textViewLongitude.text.toString().toDouble()
        )

        val advertisement = Advertisement(
            editTextFirmName.text.toString(),
            location,
            editTextPromotionContent.text.toString(),
            editTextPromotionDuration.text.toString().toLong()
        )

        DatabaseConnection.addAd(advertisement, onSuccessListener, onFailureListener)

        finish()
    }

    private val onClickCancelButton = View.OnClickListener {
        finish()
    }

    private val onClickImageButtonMarker = View.OnClickListener {
        val intent = Intent(this@NewAdActivity, MapsActivity::class.java)

        startActivityForResult(intent, REQUEST_CODE_LAT_LNG)
    }

    private val onSuccessListener = OnSuccessListener<DocumentReference> {
        Log.i(TAG, "DocumentSnapshot added with ID: ${it.id}")
    }

    private val onFailureListener = OnFailureListener {
        Log.e(TAG, "Error adding document", it)
    }
}
