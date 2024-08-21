package com.khomeapps.gender

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.khomeapps.gender.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    var selectedGender = MutableLiveData("")
    val options = mutableListOf<String>()
    private var _optionsLoaded = MutableLiveData(false)
    val optionsLoaded: LiveData<Boolean> = _optionsLoaded
    private var _voteRegistered = MutableLiveData(false)
    val voteRegistered: LiveData<Boolean> = _voteRegistered
    fun registerVote() {
        val vote = FieldValue.increment(1)
        db.collection("GenderOptions").document(selectedGender.value!!).update("count", vote)
            .addOnSuccessListener {
                _voteRegistered.postValue(true)
                db.collection("TotalVoteCount").document("Votes").update("count", vote)
            }
    }

    fun getOptions() {
        db.collection("GenderOptions")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    options.add(doc.id)
                }
                _optionsLoaded.postValue(true)
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}