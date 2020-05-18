package com.khomeapps.gender.ui.onboarding.start

import android.os.Bundle
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.ActivityOnboardingStartBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.onboarding.start.fragment.BoardingStartFragment
import org.koin.android.ext.android.get

class OnBoardingStartActivity :
    BaseActivity<OnBoardingStartViewModel, ActivityOnboardingStartBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_onboarding_start

    override fun getViewModelType(): OnBoardingStartViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentProcessor.add(BoardingStartFragment())
    }
}