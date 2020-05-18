package com.khomeapps.gender.module

import com.khomeapps.gender.ui.base.BaseViewModel
import com.khomeapps.gender.ui.onboarding.permissions.OnBoardingPermissionViewModel
import com.khomeapps.gender.ui.onboarding.permissions.fragment.BoardingSelectViewModel
import com.khomeapps.gender.ui.onboarding.permissions.fragment.BoardingCalculateViewModel
import com.khomeapps.gender.ui.onboarding.start.OnBoardingStartViewModel
import com.khomeapps.gender.ui.onboarding.start.fragment.BoardingStartViewModel
import com.khomeapps.gender.ui.result.ResultsViewModel
import com.khomeapps.gender.utils.SavedDataManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { SavedDataManager(androidContext()) }

    viewModel { BaseViewModel() }
    viewModel { OnBoardingStartViewModel() }
    viewModel { BoardingStartViewModel() }
    viewModel { OnBoardingPermissionViewModel() }
    viewModel { BoardingCalculateViewModel() }
    viewModel { BoardingSelectViewModel() }
    viewModel { ResultsViewModel() }
}