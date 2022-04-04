package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "CrimeDetailViewModel"

class CrimeDetailViewModel : ViewModel() {

    private val crimeRepository: CrimeRepository = CrimeRepository.get()
    private val crimeIdLivedata = MutableLiveData<UUID>()
    private var _crimeLiveData = MutableLiveData<Crime?>()
    private var crime: Crime? = null
    var suspect: String? = null


    val crimeLiveData = Transformations.switchMap(crimeIdLivedata) { crimeId ->
        crimeRepository.getCrime(crimeId)
    }

    fun loadCrime(crimeId: UUID) {
        crimeIdLivedata.value = crimeId
    }

    fun saveCrime(crime: Crime) {
        Log.d(TAG, "Crime: $crime")
        crimeRepository.updateCrime(crime)
    }

    fun saveSuspect(suspect: String?) {
        this.suspect = suspect
        Log.e(TAG, "save suspect: ${this.suspect}")
    }

    fun loadSuspect(crime: Crime): Crime {
        return if (suspect == null) {
            crime
        } else {
            crime.suspect = suspect!!
            crime
        }
    }
}