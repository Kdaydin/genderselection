package com.khomeapps.gender

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.Observable
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khomeapps.gender.databinding.ActivityMainBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.result.ResultsActivity
import kotlin.math.abs

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = BuildConfig.AD_ID
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        binding?.chipGroup?.setOnCheckedChangeListener { _, checkedId ->
            viewModel?.selectedGender?.value = viewModel?.options?.get(
                abs(checkedId) - 1
            )
        }
        viewModel?.optionsLoaded?.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel?.optionsLoaded?.get() == true)
                    setOptions()
            }

        })
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                startActivity(
                    Intent(
                        this@MainActivity,
                        ResultsActivity::class.java
                    ).putExtra("Selection", viewModel?.selectedGender?.value!!)
                )
                finish()
            }
        }


        viewModel?.voteRegistered?.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel?.voteRegistered?.get() == true)
                    run {
                        val builder = MaterialAlertDialogBuilder(this@MainActivity)
                        builder.setMessage("Thanks For Voting!")
                            .setPositiveButton("See Results") { _, _ ->

                                if (mInterstitialAd.isLoaded) {
                                    mInterstitialAd.show()
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                                }
                            }
                        builder.show()
                    }
            }

        })

        viewModel?.getOptions()

    }


    private fun setOptions() {
        viewModel?.options?.forEachIndexed { index, opt ->
            val genderChip = LayoutInflater.from(this)
                .inflate(R.layout.item_gender_chip, binding?.chipGroup, false) as Chip
            genderChip.text = opt
            genderChip.id = index+1
            binding?.chipGroup?.addView(genderChip)
        }
    }

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun getViewModelType(): MainViewModel = MainViewModel()
}
