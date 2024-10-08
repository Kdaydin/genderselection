package com.khomeapps.gender.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khomeapps.gender.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor() : BaseViewModel() {
    var selectedGender: String = ""
    var selectedCount: Long = 0
    var totalCount: Long = 0
    var _isShowGraph = MutableLiveData(false)
    val isShowGraph: LiveData<Boolean> = _isShowGraph
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
                _isShowGraph.postValue(true)
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }


}