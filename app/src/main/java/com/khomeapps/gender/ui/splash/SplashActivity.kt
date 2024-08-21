package com.khomeapps.gender.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.khomeapps.gender.MainActivity
import com.khomeapps.gender.ui.onboarding.start.OnBoardingStartActivity
import com.khomeapps.gender.utils.SavedDataManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var savedDataManager: SavedDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = if (savedDataManager.isBoardingCompleted() == true) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, OnBoardingStartActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}