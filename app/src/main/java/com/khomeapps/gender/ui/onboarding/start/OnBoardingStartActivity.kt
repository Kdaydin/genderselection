package com.khomeapps.gender.ui.onboarding.start

import android.os.Bundle
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.ActivityOnboardingStartBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.onboarding.start.fragment.BoardingStartFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingStartActivity :
    BaseActivity() {
    private var binding: ActivityOnboardingStartBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingStartBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, BoardingStartFragment())
            .commit()
    }
}