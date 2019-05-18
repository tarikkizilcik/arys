package org.example.arys.data

data class Advertisement(
    var FirmaID: String,
    var FirmaAdi: String,
    var FirmaLokasyon: String,
    var KampanyaIcerik: String,
    var KampanyaSuresi: Long
) {
    companion object {
        const val EXTRA_FIRMA_ID = "FIRMA_ID"
        const val EXTRA_FIRMA_ADI = "FIRMA_ADI"
        const val EXTRA_FIRMA_LOKASYON = "FIRMA_LOKASYON"
        const val EXTRA_KAMPANYA_ICERIK = "KAMPANYA_ICERIK"
        const val EXTRA_KAMPANYA_SURESI = "KAMPANYA_SURESI"
    }
}
