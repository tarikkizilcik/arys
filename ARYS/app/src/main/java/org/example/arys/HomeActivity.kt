package org.example.arys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        requestPermissions()

        buttonNewAd.setOnClickListener(onClickNewAd)
    }

    private val onClickNewAd = View.OnClickListener {
        val intent = Intent(this, NewAdActivity::class.java)
        startActivity(intent)
    }

    private fun onLocationPermissionDenied() {
        Toast.makeText(baseContext, getString(R.string.permission_fine_location_needed), Toast.LENGTH_LONG).show()
        finish()
    }

    private fun requestPermissions() {
        // Here, thisActivity is the current activity
        when (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )) {
            PackageManager.PERMISSION_GRANTED -> {
                // Permission has already been granted
            }
            else -> {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    onLocationPermissionDenied()
                }

                // Request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            onLocationPermissionDenied()
        }
    }
}
