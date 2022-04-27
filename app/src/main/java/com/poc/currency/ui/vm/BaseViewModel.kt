package com.poc.currency.ui.vm

import androidx.lifecycle.ViewModel
import com.poc.currency.domain.response.ErrorEntity
import com.poc.currency.utils.SingleLiveData

open class BaseViewModel : ViewModel() {
    val loadingEvent = SingleLiveData<Boolean>()
    val errorEvent = SingleLiveData<ErrorEntity.Error?>()

    /**
     * Method to show Loading dialog
     */
    fun showLoading(shouldShow: Boolean) {
        loadingEvent.value = shouldShow
    }

    /**
     * Method to show error
     */
    fun showError(error: ErrorEntity.Error?) {
        errorEvent.value = error
    }
}