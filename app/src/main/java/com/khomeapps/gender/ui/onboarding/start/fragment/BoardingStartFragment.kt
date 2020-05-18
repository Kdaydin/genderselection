package com.khomeapps.gender.ui.onboarding.start.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.FragmentBoardingStartBinding
import com.khomeapps.gender.ui.base.BaseFragment
import com.khomeapps.gender.ui.onboarding.permissions.OnBoardingPermissionActivity
import org.koin.android.ext.android.get

class BoardingStartFragment : BaseFragment<BoardingStartViewModel, FragmentBoardingStartBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_boarding_start

    override fun getViewModelType(): BoardingStartViewModel = get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnStart?.setOnClickListener {
            val i = Intent(context, OnBoardingPermissionActivity::class.java)
            startActivity(i)
            activity?.finish()
        }
    }
}