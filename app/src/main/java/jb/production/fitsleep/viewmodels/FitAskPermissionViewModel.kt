package jb.production.fitsleep.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FitAskPermissionViewModel @Inject constructor() : ViewModel() {

    val reconnectClick = MutableLiveData<Int>()
    var i = 0

    fun reconnect() {
        i++
        Log.e("$$$", "posting value")
        reconnectClick.postValue(i)
    }

}