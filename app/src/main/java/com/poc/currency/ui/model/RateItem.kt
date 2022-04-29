package com.poc.currency.ui.model


class RateItem(
    val rate: Double?,
    private val date: String,
    private val fromCurrency: String,
    private val toCurrency: String
) {
    fun getDateValue(): String {
        return "On $date"
    }

    fun getToCurrencyValue(): String {
        return "1 $fromCurrency = $rate $toCurrency"
    }

    private fun getRate(): String {
        return rate.toString()
    }
}