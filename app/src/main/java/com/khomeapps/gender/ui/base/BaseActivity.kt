package com.khomeapps.gender.ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khomeapps.gender.BR
import com.khomeapps.gender.R
import com.khomeapps.gender.application.AppConstans
import com.khomeapps.gender.utils.SavedDataManager
import org.koin.android.ext.android.get
import java.lang.ref.WeakReference

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {
    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun getViewModelType(): VM

    protected var viewModel: VM? = null
    protected var binding: DB? = null
    var fragmentProcessor = FragmentProcessor(
        WeakReference(supportFragmentManager),
        R.id.mainContainer,
        WeakReference(this)
    )
    var dataManager: SavedDataManager = get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        viewModel = getViewModelType()
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.lifecycleOwner = this
        viewModel?.onCreate()

    }

    override fun onResume() {
        super.onResume()
        viewModel?.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel?.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel?.onStart()
    }

    override fun onDestroy() {
        binding?.unbind()
        binding?.lifecycleOwner = null
        binding = null
        super.onDestroy()
    }

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
            AppConstans.Permissions.LOCATION -> ""
            AppConstans.Permissions.NOTIFICATION -> ""
            AppConstans.Permissions.WRITE_EXTERNAL -> "GenderSelection App Needs Permission"
            else -> ""
        }

    }

    private fun getPermissionRequestMessage(requestCode: Int): String {
        return when (requestCode) {
            AppConstans.Permissions.LOCATION -> ""
            AppConstans.Permissions.NOTIFICATION -> ""
            AppConstans.Permissions.WRITE_EXTERNAL -> "We need your permission to save chart image to your phone."
            else -> ""
        }
    }
}
