package com.khomeapps.gender.ui.onboarding.permissions.fragment

import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.FragmentBoardingCalculateBinding
import com.khomeapps.gender.ui.base.BaseFragment
import org.koin.android.ext.android.get

class BoardingCalculateFragment :
    BaseFragment<BoardingCalculateViewModel, FragmentBoardingCalculateBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_boarding_calculate

    override fun getViewModelType(): BoardingCalculateViewModel = get()

}