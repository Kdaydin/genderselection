package com.khomeapps.gender

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khomeapps.gender.databinding.ActivityMainBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.result.ResultsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null
    val viewModel: MainViewModel by viewModels()
    private var mInterstitialAd: InterstitialAd? = null
    val s = "AD_LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        InterstitialAd.load(
            this,
            BuildConfig.AD_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    mInterstitialAd = ad
                }
            })
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                startActivity(
                    Intent(
                        this@MainActivity,
                        ResultsActivity::class.java
                    ).putExtra("Selection", viewModel.selectedGender.value!!)
                )
                finish()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
            }
        }

        mInterstitialAd?.setOnPaidEventListener {
            Log.d("TAG", "onCreate: ")
        }
        binding?.adView?.loadAd(AdRequest.Builder().build())

        binding?.btnVote?.setOnClickListener {
            viewModel.registerVote()
        }
        viewModel.selectedGender.observe(this) {
            binding?.btnVote?.isEnabled = it.isNotEmpty()
        }
        binding?.chipGroup?.setOnCheckedChangeListener { _, checkedId ->
            viewModel.selectedGender.value = viewModel.options.get(
                abs(checkedId) - 1
            )
        }
        viewModel.optionsLoaded.observe(this) {
            if (viewModel.optionsLoaded.value == true)
                setOptions()

        }


        viewModel?.voteRegistered?.observe(this) {
            if (viewModel?.voteRegistered?.value == true)
                run {
                    val builder = MaterialAlertDialogBuilder(this@MainActivity)
                    builder.setMessage("Thanks For Voting!")
                        .setPositiveButton("See Results") { _, _ ->

                            mInterstitialAd?.show(this) ?: run {
                                Log.d("TAG", "The interstitial wasn't loaded yet.")
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        ResultsActivity::class.java
                                    ).putExtra("Selection", viewModel.selectedGender?.value!!)
                                )
                                finish()
                            }
                        }
                    builder.show()
                }
        }

        viewModel.getOptions()

    }


    private fun setOptions() {
        viewModel.options?.forEachIndexed { index, opt ->
            val genderChip = LayoutInflater.from(this)
                .inflate(R.layout.item_gender_chip, binding?.chipGroup, false) as Chip
            genderChip.text = opt
            genderChip.id = index + 1
            binding?.chipGroup?.addView(genderChip)
        }
    }
}
