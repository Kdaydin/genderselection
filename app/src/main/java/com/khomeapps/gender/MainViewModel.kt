package com.khomeapps.gender

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khomeapps.gender.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    var selectedGender = MutableLiveData("")
    val options = mutableListOf<String>()
    var optionsLoaded = ObservableField<Boolean>(false)
    var voteRegistered = ObservableField<Boolean>(false)
    fun registerVote() {
        val vote = FieldValue.increment(1)
        db.collection("GenderOptions").document(selectedGender.value!!).update("count", vote)
            .addOnSuccessListener {
                voteRegistered.set(true)
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
                optionsLoaded.set(true)
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}