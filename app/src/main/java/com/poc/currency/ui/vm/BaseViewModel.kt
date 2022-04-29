package com.poc.currency.ui.vm

import androidx.lifecycle.ViewModel
import com.poc.currency.domain.response.ErrorEntity
import com.poc.currency.utils.SingleLiveData

/**
 * View model class can hold all the common work used in view models
 * Live data events for showing/hiding progress bar, error event
 * Can contains any generic event which are used through out the app
 */
open class BaseViewModel : ViewModel() {
    val loadingEvent = SingleLiveData<Boolean>()
    val errorEvent = SingleLiveData<ErrorEntity.Error?>()

    /**
     * Method to show Loading dialog
     * @param shouldShow -> flag used to show/hide progress bar
     */
    fun showLoading(shouldShow: Boolean) {
        loadingEvent.value = shouldShow
    }

    /**
     * Method to show error
     * @param error -> contains error message
     */
    fun showError(error: ErrorEntity.Error?) {
        errorEvent.value = error
    }
}