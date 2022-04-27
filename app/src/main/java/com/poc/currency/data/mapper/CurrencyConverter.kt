package com.poc.currency.data.mapper

import com.poc.currency.data.remote.dto.CurrencyDto
import com.poc.currency.data.remote.dto.ErrorDto
import com.poc.currency.domain.response.ErrorEntity
import com.poc.currency.domain.response.currency.CurrencyEntity


fun CurrencyDto.CurrencyList.map(): CurrencyEntity.CurrencyList {

    val currencyList = arrayListOf<CurrencyEntity.Currency>()
    currenciesJson?.entrySet()?.filter { !it.key.isNullOrEmpty() }?.mapNotNullTo(currencyList) {
        CurrencyEntity.Currency(it.key, it.value.asString)
    }
    return CurrencyEntity.CurrencyList(
        isSuccess ?: false,
        currencyList = currencyList,
        if (this.isSuccess == true) null else error?.map()
    )
}

fun CurrencyDto.ConversionResultDto.map(): CurrencyEntity.CurrencyConversionResult {
    val currencyList = arrayListOf<CurrencyEntity.Currency>()
    ratesJson?.entrySet()?.filter { !it.key.isNullOrEmpty() }?.mapNotNullTo(currencyList) {
        CurrencyEntity.Currency(code = it.key, rate = it.value.asDouble, value = it.key)
    }
    return CurrencyEntity.CurrencyConversionResult(
        isSuccess = success ?: false,
        fromCurrency = fromCurrency ?: "",
        currencyListWithRates = currencyList,
        dateString = dateString ?: "",
        if (this.success == true) null else error?.map()
    )
}

fun ErrorDto.Error.map(): ErrorEntity.Error {
    return ErrorEntity.Error(
        errorCode = code,
        errorMessage = if (!message.isNullOrEmpty()) message else type
    )
}