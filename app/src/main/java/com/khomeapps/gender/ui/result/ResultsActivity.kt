package com.khomeapps.gender.ui.result

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.Observable
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.ActivityResultsBinding
import com.khomeapps.gender.ui.base.BaseActivity
import org.koin.android.ext.android.get

class ResultsActivity : BaseActivity<ResultsViewModel, ActivityResultsBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_results

    override fun getViewModelType(): ResultsViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra("Selection").let {
            viewModel?.selectedGender = it!!
        }
        viewModel?.isShowGraph?.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel?.isShowGraph?.get() == true)
                    showGraph()
            }

        })
        viewModel?.getSelected()

    }

    private fun showGraph() {
        val chart = binding?.pieChart
        val selected = PieEntry(viewModel?.selectedCount?.toFloat()!!, viewModel?.selectedGender)
        val others =
            PieEntry((viewModel?.totalCount!! - viewModel?.selectedCount!!).toFloat(), "Others")
        val list: MutableList<PieEntry> = mutableListOf(selected, others)
        val dataSet = PieDataSet(list, "")
        dataSet.sliceSpace = 2F
        dataSet.valueFormatter = PercentFormatter()
        dataSet.valueTextColor = R.color.white
        dataSet.valueTextSize = 16F
        dataSet.valueTypeface = ResourcesCompat.getFont(this, R.font.tiempos_headline_bold_italic)
        chart?.setUsePercentValues(true)
        chart?.data = PieData(dataSet)
        val centerText = SpannableString("Gender Populations")
        centerText.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.white, this.theme)),
            0,
            centerText.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //centerText.setSpan(AbsoluteSizeSpan(),0,centerText.length,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        chart?.centerText = centerText
        chart?.setCenterTextSize(24F)
        chart?.setHoleColor(android.R.color.transparent)
        chart?.legend?.isEnabled = false
        chart?.description = null
        chart?.invalidate()
        chart?.visibility = View.VISIBLE
        binding?.resultText?.text = "*Shown For ${viewModel?.totalCount} Total Votes"
    }
}