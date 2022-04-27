package com.poc.currency.domain.usecases

import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.repository.CurrencyRepository
import com.poc.currency.domain.response.currency.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyUseCaseImpl @Inject constructor(private val currencyRepository: CurrencyRepository) : CurrencyUseCase {
    override fun getSupportedCurrencies(): Flow<ResultState<CurrencyEntity.CurrencyList>> {
        return currencyRepository.getSupportedCurrencies()
    }

    override fun getCurrencyConversion(
        fromCurrency: String,
        toCurrency: List<String>
    ): Flow<ResultState<CurrencyEntity.CurrencyConversionResult>> {
        return currencyRepository.getCurrencyConversion(fromCurrency, toCurrency)
    }

    override fun getCurrencyConversionByDays(
        days: Int,
        fromCurrency: String,
        toCurrency: List<String>
    ): Flow<ResultState<List<CurrencyEntity.CurrencyConversionResult>>> {
        return currencyRepository.getCurrencyConversionByDays(days, fromCurrency, toCurrency)
    }
}