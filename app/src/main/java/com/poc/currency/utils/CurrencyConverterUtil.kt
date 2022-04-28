package com.poc.currency.utils

import com.poc.currency.domain.response.currency.CurrencyEntity
import kotlin.math.round


object CurrencyConverterUtil {
    fun getConvertedAmount(
        fromCurrency: String,
        toCurrency: String,
        currencyList: List<CurrencyEntity.Currency>,
        amount: Double = 1.0
    ): Double? {
        val fromCurrencyEuroRate: Double? = currencyList.find { it.code == fromCurrency }?.rate
        val toCurrencyEuroRate: Double? = currencyList.find { it.code == toCurrency }?.rate

        return if (null == fromCurrencyEuroRate || null == toCurrencyEuroRate) {
            null
        } else {
            (toCurrencyEuroRate / fromCurrencyEuroRate * amount).roundTo(2)
        }
    }

    private fun Double.roundTo(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}