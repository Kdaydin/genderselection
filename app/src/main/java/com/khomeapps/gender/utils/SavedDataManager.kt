package com.khomeapps.gender.utils

import android.content.Context
import com.khomeapps.gender.utils.diskManager.PersistentGenericData

class SavedDataManager(context: Context) {

    private var boardingPersistentGenericData =
        PersistentGenericData<Boolean>(context, "Boarding.obj")


    companion object {
        var boarding: Boolean? = null
    }


    fun setBoardingComplete(isComplete: Boolean) {
        boarding = isComplete
        boardingPersistentGenericData.saveFieldByName(boarding)
    }

    fun isBoardingCompleted(): Boolean? {
        if (boarding == null) {
            boarding = boardingPersistentGenericData.retrieveFieldByName() as Boolean?
        }
        return boarding
    }

}