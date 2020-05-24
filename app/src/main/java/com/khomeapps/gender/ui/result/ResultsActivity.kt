package com.khomeapps.gender.ui.result

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.Observable
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.khomeapps.gender.R
import com.khomeapps.gender.databinding.ActivityResultsBinding
import com.khomeapps.gender.ui.base.BaseActivity
import org.koin.android.ext.android.get
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ResultsActivity : BaseActivity<ResultsViewModel, ActivityResultsBinding>() {

    private var chart: PieChart? = null

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
        binding?.shareTwitter?.setOnClickListener {
            val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            shareOnTwitter(
                "I'm a ${viewModel?.selectedGender} with ${(viewModel?.totalCount!! - viewModel?.selectedCount!!)} others like me. Vote now and share how many of us out there!",
                getLocalBitmapUri(chart?.chartBitmap)
            )
        }

        binding?.shareOther?.setOnClickListener {
            val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            shareOnOther(
                "I'm a ${viewModel?.selectedGender} with ${(viewModel?.totalCount!! - viewModel?.selectedCount!!)} others like me. Vote now and share how many of us out there!",
                getLocalBitmapUri(chart?.chartBitmap)
            )
        }

    }

    private fun showGraph() {
        chart = binding?.pieChart
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

        binding?.resultText?.text =
            "*${(viewModel?.totalCount!! - viewModel?.selectedCount!!)} out of ${viewModel?.totalCount} people are with you!"
    }

    private fun shareOnTwitter(textBody: String?, fileUri: Uri?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.setPackage("com.twitter.android")
        shareIntent.putExtra(Intent.EXTRA_TEXT, textBody ?: "")

        if (fileUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.type = "image/*";
        }

        try {
            startActivity(shareIntent);
        } catch (ex: Exception) {
            Log.d("Exception:", ex.message ?: "")
        }
    }

    private fun shareOnOther(textBody: String?, fileUri: Uri?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, textBody ?: "")

        if (fileUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.type = "image/*";
        }

        try {
            startActivity(shareIntent);
        } catch (ex: Exception) {
            Log.d("Exception:", ex.message ?: "")
        }
    }


    private fun getLocalBitmapUri(bmp: Bitmap?): Uri? {
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}