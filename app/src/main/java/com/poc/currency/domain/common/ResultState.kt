package com.poc.currency.domain.common

import com.poc.currency.domain.response.ErrorEntity

sealed class ResultState<T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error<T>(val error: ErrorEntity.Error?) : ResultState<T>()
}