package org.example.arys.data

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue

data class Advertisement(
    var firmName: String,
    var firmLocation: LatLng,
    var promotionContent: String,
    var promotionDuration: Long,
    var timestamp: FieldValue = FieldValue.serverTimestamp()
)
