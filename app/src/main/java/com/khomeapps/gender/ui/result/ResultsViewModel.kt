package com.khomeapps.gender.ui.result

import android.util.Log
import androidx.databinding.ObservableField
import com.khomeapps.gender.ui.base.BaseViewModel

class ResultsViewModel : BaseViewModel() {
    var selectedGender: String = ""
    var selectedCount: Long = 0
    var totalCount: Long = 0
    var isShowGraph = ObservableField(false)
    fun getSelected() {
        db.collection("GenderOptions").document(selectedGender)
            .get()
            .addOnSuccessListener { result ->
                selectedCount = result["count"] as Long
                getTotalCount()
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    private fun getTotalCount() {
        db.collection("TotalVoteCount").document("Votes")
            .get()
            .addOnSuccessListener { result ->
                totalCount = result["count"] as Long
                isShowGraph.set(true)
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }


}