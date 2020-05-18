package com.khomeapps.gender.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.khomeapps.gender.BR
import com.khomeapps.gender.R
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
}
