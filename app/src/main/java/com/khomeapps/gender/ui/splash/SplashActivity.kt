package com.khomeapps.gender.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.khomeapps.gender.MainActivity
import com.khomeapps.gender.ui.onboarding.start.OnBoardingStartActivity
import com.khomeapps.gender.utils.SavedDataManager
import org.koin.android.ext.android.get


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataManager:SavedDataManager = get()
        val intent = if (dataManager.isBoardingCompleted() == true) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, OnBoardingStartActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}