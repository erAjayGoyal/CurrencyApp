package com.poc.currency.domain.repository

import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.response.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getSupportedCurrencies(): Flow<ResultState<CurrencyEntity.CurrencyList>>

    fun getCurrencyConversion(
        fromCurrency: String,
        toCurrency: List<String>
    ): Flow<ResultState<CurrencyEntity.CurrencyConversionResult>>

    fun getCurrencyConversionByDays(
        days: Int,
        fromCurrency: String,
        toCurrency: List<String>
    ): Flow<ResultState<List<CurrencyEntity.CurrencyConversionResult>>>
}