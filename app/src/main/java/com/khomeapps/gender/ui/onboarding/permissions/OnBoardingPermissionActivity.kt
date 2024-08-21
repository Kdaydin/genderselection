package com.khomeapps.gender.ui.onboarding.permissions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.khomeapps.gender.MainActivity
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.ActivityOnboardingPermissionBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.onboarding.permissions.fragment.BoardingCalculateFragment
import com.khomeapps.gender.ui.onboarding.permissions.fragment.BoardingSelectFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingPermissionActivity :
    BaseActivity() {
    lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    var currentPage = 0

    val viewModel: OnBoardingPermissionViewModel by viewModels()

    private var binding: ActivityOnboardingPermissionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingPermissionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val selectFragment = BoardingSelectFragment()
        val calculateFragment = BoardingCalculateFragment()
        val pagerFragmentList =
            listOf<Fragment>(selectFragment, calculateFragment)
        binding?.vpBoarding?.isUserInputEnabled = false
        binding?.vpBoarding?.isSaveEnabled = false
        pageChangeCallback = object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (pagerFragmentList[position]) {
                    is BoardingSelectFragment -> {
                        binding?.btnStart?.text = getString(R.string.boarding_select_button)
                    }

                    is BoardingCalculateFragment -> {
                        binding?.btnStart?.text = getString(R.string.boarding_calculate_button)
                    }
                }
            }
        }
        binding?.vpBoarding?.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (pagerFragmentList[position]) {
                    is BoardingSelectFragment -> {
                        selectFragment
                    }

                    is BoardingCalculateFragment -> {
                        calculateFragment
                    }

                    else -> Fragment()
                }
            }

            override fun getItemCount(): Int {
                return pagerFragmentList.size
            }
        }
        binding?.vpBoarding?.registerOnPageChangeCallback(pageChangeCallback)

        binding?.btnStart?.setOnClickListener {
            if (currentPage < 1) {
                currentPage++
                binding?.vpBoarding?.setCurrentItem(currentPage, true)
            } else {
                val i = Intent(baseContext, MainActivity::class.java)
                savedDataManager.setBoardingComplete(true)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (currentPage > 0) {
            currentPage--
            binding?.vpBoarding?.setCurrentItem(currentPage, true)
        } else {
            finish()
        }
    }
}