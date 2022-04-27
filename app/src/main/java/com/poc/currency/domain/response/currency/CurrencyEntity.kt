package com.poc.currency.domain.response.currency

import com.poc.currency.domain.response.ErrorEntity

sealed class CurrencyEntity {
    data class CurrencyList(
        val isSuccess: Boolean = false,
        val currencyList: List<Currency>,
        val error: ErrorEntity.Error? = null
    ) : CurrencyEntity()

    data class Currency(val code: String, val value: String, var rate: Double? = null) : CurrencyEntity() {
        fun getNonNullRateString() : String {
            return rate.toString() ?: ""
        }
    }

    data class CurrencyConversionResult(
        val isSuccess: Boolean = false,
        val fromCurrency: String,
        val currencyListWithRates: List<Currency>,
        val dateString: String,
        val error: ErrorEntity.Error? = null
    ) : CurrencyEntity()
}
