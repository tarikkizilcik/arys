package org.example.arys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ALL_PERMISSIONS = 0
    }

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

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

    private fun hasPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val pendingPermissions = permissions.distinctBy { s -> !hasPermission(s) }.toTypedArray()

        pendingPermissions.forEach {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    it
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                onLocationPermissionDenied()
            }
        }


        // Request the permission.
        ActivityCompat.requestPermissions(
            this,
            pendingPermissions,
            REQUEST_CODE_ALL_PERMISSIONS
        )

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_ALL_PERMISSIONS
            && grantResults.any { i -> i == PackageManager.PERMISSION_DENIED }
        ) {
            onLocationPermissionDenied()
        }
    }
}
