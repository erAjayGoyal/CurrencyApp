package com.poc.currency.utils

object Constants {

    const val HISTORY_DATE_SIZE = 3
    const val BASE_CURRENCY = "EUR"
    fun getPopularCountryCurrencies() = arrayListOf(
        "USD",
        "AUD",
        "CAD",
        "PLN",
        "MXN",
        "INR",
        "AED",
        "AFN",
        "ALL",
        "AMD",
    )
}