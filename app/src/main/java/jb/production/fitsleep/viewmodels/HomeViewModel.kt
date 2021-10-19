package jb.production.fitsleep.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jb.production.fitsleep.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val goToVisualization = SingleLiveEvent<Unit>()

    fun toVisualization() {
        goToVisualization.call()
    }
}