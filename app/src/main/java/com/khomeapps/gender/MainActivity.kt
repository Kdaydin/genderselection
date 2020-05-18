package com.khomeapps.gender

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.Observable
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khomeapps.gender.databinding.ActivityMainBinding
import com.khomeapps.gender.ui.base.BaseActivity
import com.khomeapps.gender.ui.result.ResultsActivity
import kotlin.math.abs

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        viewModel?.voteRegistered?.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel?.voteRegistered?.get() == true)
                    run {
                        val builder = MaterialAlertDialogBuilder(this@MainActivity)
                        builder.setMessage("Thanks For Voting!")
                            .setPositiveButton("See Results") { _, _ ->
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        ResultsActivity::class.java
                                    ).putExtra("Selection", viewModel?.selectedGender?.value!!)
                                )
                                finish()
                            }
                        builder.show()
                    }
            }

        })

        viewModel?.getOptions()

    }

    private fun setOptions() {
        viewModel?.options?.forEach { opt ->
            val genderChip = LayoutInflater.from(this)
                .inflate(R.layout.item_gender_chip, binding?.chipGroup, false) as Chip
            genderChip.text = opt
            binding?.chipGroup?.addView(genderChip)
        }
        binding?.chipGroup?.addView(Chip(this,null,R.style.Widget_MaterialComponents_Chip_Filter))
    }

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun getViewModelType(): MainViewModel = MainViewModel()
}
