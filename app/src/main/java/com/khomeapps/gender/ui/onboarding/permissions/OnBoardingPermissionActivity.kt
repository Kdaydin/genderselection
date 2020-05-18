package com.khomeapps.gender.ui.onboarding.permissions

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.khomeapps.gender.MainActivity
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.ActivityOnboardingPermissionBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.onboarding.permissions.fragment.*
import org.koin.android.ext.android.get

class OnBoardingPermissionActivity :
    BaseActivity<OnBoardingPermissionViewModel, ActivityOnboardingPermissionBinding>() {
    lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    var currentPage = 0
    override fun getLayoutRes(): Int = R.layout.activity_onboarding_permission

    override fun getViewModelType(): OnBoardingPermissionViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectFragment = BoardingSelectFragment()
        val calculateFragment = BoardingCalculateFragment()
        selectFragment.viewModel = BoardingSelectViewModel()
        calculateFragment.viewModel = BoardingCalculateViewModel()
        val pagerFragmentList =
            listOf<Fragment>(selectFragment,calculateFragment)
        binding?.vpBoarding?.isUserInputEnabled = false
        binding?.vpBoarding?.isSaveEnabled = false
        pageChangeCallback = object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (pagerFragmentList[position]) {
                    is BoardingSelectFragment -> {
                        viewModel?.btnText?.set(getString(R.string.boarding_select_button))
                    }
                    is BoardingCalculateFragment -> {
                        viewModel?.btnText?.set(getString(R.string.boarding_calculate_button))
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
                dataManager.setBoardingComplete(true)
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