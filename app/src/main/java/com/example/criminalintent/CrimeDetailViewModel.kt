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
    private val crime: Crime? = null


    fun initLiveData(): LiveData<Crime?> {
        var crimeLiveData: LiveData<Crime?> = _crimeLiveData
        if (crime == null) {
            crimeLiveData = Transformations.switchMap(crimeIdLivedata) { crimeId ->
                crimeRepository.getCrime(crimeId)
            }
        } else {
            _crimeLiveData.value = crime
        }
        return crimeLiveData
    }


    fun loadCrime(crimeId: UUID) {
        crimeIdLivedata.value = crimeId
    }

    fun saveCrime(crime: Crime) {
        Log.d(TAG, "Crime: $crime")
        crimeRepository.updateCrime(crime)
    }
}