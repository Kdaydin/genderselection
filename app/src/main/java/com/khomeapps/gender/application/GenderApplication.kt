package com.khomeapps.gender.application

import android.app.Application
import com.khomeapps.gender.module.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GenderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GenderApplication)
            modules(appModules)
        }
    }
}