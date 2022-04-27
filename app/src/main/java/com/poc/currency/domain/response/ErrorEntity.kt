package com.poc.currency.domain.response

sealed class ErrorEntity {
    data class Error(val errorCode:Any?, val errorMessage: String? = "")
}