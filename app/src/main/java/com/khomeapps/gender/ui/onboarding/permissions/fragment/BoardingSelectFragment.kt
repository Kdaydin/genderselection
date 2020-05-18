package com.khomeapps.gender.ui.onboarding.permissions.fragment

import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.FragmentBoardingSelectBinding
import com.khomeapps.gender.ui.base.BaseFragment
import org.koin.android.ext.android.get

class BoardingSelectFragment :
    BaseFragment<BoardingSelectViewModel, FragmentBoardingSelectBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_boarding_select

    override fun getViewModelType(): BoardingSelectViewModel = get()

}