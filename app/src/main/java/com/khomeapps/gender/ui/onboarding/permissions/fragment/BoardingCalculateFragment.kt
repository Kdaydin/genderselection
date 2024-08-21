package com.khomeapps.gender.ui.onboarding.permissions.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.khomeapps.gender.databinding.FragmentBoardingCalculateBinding
import com.khomeapps.gender.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingCalculateFragment :
    BaseFragment() {
    private var binding: FragmentBoardingCalculateBinding? = null
    val viewModel: BoardingCalculateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardingCalculateBinding.inflate(inflater, container, false)
        return binding?.root
    }
}