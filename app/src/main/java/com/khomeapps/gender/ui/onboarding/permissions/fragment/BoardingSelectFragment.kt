package com.khomeapps.gender.ui.onboarding.permissions.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.khomeapps.gender.databinding.FragmentBoardingSelectBinding
import com.khomeapps.gender.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingSelectFragment :
    BaseFragment() {
    private var binding: FragmentBoardingSelectBinding? = null
    val viewModel: BoardingSelectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardingSelectBinding.inflate(inflater, container, false)
        return binding?.root
    }
}