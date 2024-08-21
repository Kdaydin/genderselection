package com.khomeapps.gender.ui.onboarding.start.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khomeapps.gender.databinding.FragmentBoardingStartBinding
import com.khomeapps.gender.ui.base.BaseFragment
import com.khomeapps.gender.ui.onboarding.permissions.OnBoardingPermissionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingStartFragment : BaseFragment() {

    private var binding: FragmentBoardingStartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardingStartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnStart?.setOnClickListener {
            val i = Intent(context, OnBoardingPermissionActivity::class.java)
            startActivity(i)
            activity?.finish()
        }
    }
}