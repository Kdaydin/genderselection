package com.khomeapps.gender.ui.base

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khomeapps.gender.application.AppConstants
import com.khomeapps.gender.utils.SavedDataManager
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {


    @Inject
    lateinit var savedDataManager: SavedDataManager

    fun checkAndRequestPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission
                )
            ) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getPermissionRequestHeader(requestCode))
                    .setMessage(getPermissionRequestMessage(requestCode))
                    .setNegativeButton("Cancel") { _, _ ->
                    }
                    .setPositiveButton("Allow") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(permission),
                            requestCode
                        )
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
                )
            }
        } else {
            return true
        }
        return false
    }


    private fun getPermissionRequestHeader(requestCode: Int): String {
        return when (requestCode) {
            AppConstants.Permissions.LOCATION -> ""
            AppConstants.Permissions.NOTIFICATION -> ""
            AppConstants.Permissions.WRITE_EXTERNAL -> "GenderSelection App Needs Permission"
            else -> ""
        }

    }

    private fun getPermissionRequestMessage(requestCode: Int): String {
        return when (requestCode) {
            AppConstants.Permissions.LOCATION -> ""
            AppConstants.Permissions.NOTIFICATION -> ""
            AppConstants.Permissions.WRITE_EXTERNAL -> "We need your permission to save chart image to your phone."
            else -> ""
        }
    }
}
