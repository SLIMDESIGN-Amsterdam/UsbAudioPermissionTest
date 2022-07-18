package com.slimdesign.usbaudiopermission

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.slimdesign.usbaudiopermission.util.checkSelfPermissionCompat
import com.slimdesign.usbaudiopermission.util.requestPermissionsCompat
import com.slimdesign.usbaudiopermission.util.shouldShowRequestPermissionRationaleCompat
import com.slimdesign.usbaudiopermission.util.showSnackbar

private const val PERMISSION_REQUEST = 0

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_layout)

        // Register a listener for the 'Fake Audio Record' button.
        findViewById<Button>(R.id.button_test_audio_permission).setOnClickListener { testAudioPermission() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permission has been granted.
                layout.showSnackbar(R.string.record_permission_granted, Snackbar.LENGTH_SHORT)
                fakeStartRecording()
            } else {
                // Permission request was denied.
                layout.showSnackbar(R.string.record_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun testAudioPermission() {
        // Check if the permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED) {
            // Permission is already available
            layout.showSnackbar(R.string.record_permission_available, Snackbar.LENGTH_SHORT)
            fakeStartRecording()
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission()
        }
    }

    /**
     * Requests the [android.Manifest.permission.CAMERA] and [android.Manifest.permission.RECORD_AUDIO] permissions.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.CAMERA) ||
            shouldShowRequestPermissionRationaleCompat(Manifest.permission.RECORD_AUDIO)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            layout.showSnackbar(R.string.record_access_required,
                Snackbar.LENGTH_INDEFINITE, android.R.string.ok) {
                requestPermissionsCompat(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                    PERMISSION_REQUEST)
            }

        } else {
            layout.showSnackbar(R.string.record_permission_not_available, Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST)
        }
    }

    private fun fakeStartRecording() {
        // Won't actually do anything here.
    }
}
