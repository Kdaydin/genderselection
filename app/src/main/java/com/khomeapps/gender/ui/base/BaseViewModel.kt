package com.khomeapps.gender.ui.base

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {
    val db = Firebase.firestore

    open fun onStart() {}

    open fun onStop() {}

    open fun onPause() {}

    open fun onCreate() {}

    open fun onResume() {}

    open fun onCreateView() {}

    open fun onViewCreated() {}
}