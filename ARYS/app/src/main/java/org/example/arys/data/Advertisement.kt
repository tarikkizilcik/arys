package org.example.arys.data

import com.google.firebase.firestore.FieldValue

data class Advertisement(
    var firmName: String,
    var firmLocationLatitude: Double,
    var firmLocationLongitude: Double,
    var promotionContent: String,
    var promotionDuration: Long,
    var timestamp: FieldValue = FieldValue.serverTimestamp()
)
