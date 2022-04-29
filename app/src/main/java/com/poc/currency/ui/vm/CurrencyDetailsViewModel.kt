package com.poc.currency.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.response.currency.CurrencyEntity
import com.poc.currency.domain.usecases.CurrencyUseCase
import com.poc.currency.ui.model.RateItem
import com.poc.currency.utils.Constants.BASE_CURRENCY
import com.poc.currency.utils.Constants.HISTORY_DATE_SIZE
import com.poc.currency.utils.Constants.getPopularCountryCurrencies
import com.poc.currency.utils.CurrencyConverterUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailsViewModel @Inject constructor(private val useCase: CurrencyUseCase) :
    BaseViewModel() {

    var historyRateList = MutableLiveData<List<RateItem>>()
    var countryCurrencyRateItems = MutableLiveData<List<CurrencyEntity.Currency>>()
    var chartHistoryRatesList = MutableLiveData<ArrayList<RateItem>>()

    /**
     * Method which gets the historic rates and
     * other currencies rates
     * @param fromCurrency -> from currency value
     * @param toCurrency -> to currency value
     */
    fun getCurrencyHistoryData(fromCurrency: String, toCurrency: String) {
        showLoading(true)
        val currencies = getPopularCountryCurrencies()
        //Added the from and to currency to get their rates
        //in base currency EUR
        currencies.add(fromCurrency)
        currencies.add(toCurrency)
        viewModelScope.launch {
            useCase.getCurrencyConversionByDays(
                HISTORY_DATE_SIZE,
                BASE_CURRENCY,
                listOf(fromCurrency, toCurrency)
            ).combine(
                useCase.getCurrencyConversion(BASE_CURRENCY, currencies)
            ) { historyData, otherRates ->
                when (historyData) {
                    is ResultState.Success -> {
                        //Calculating the rate and adding it to view
                        val historyRateItems = historyData.data.map {
                            val convertedToAmount = CurrencyConverterUtil.getConvertedAmount(
                                fromCurrency,
                                toCurrency,
                                it.currencyListWithRates
                            )
                            RateItem(
                                convertedToAmount,
                                it.dateString,
                                fromCurrency,
                                toCurrency
                            )
                        }.filter {
                            it.rate != null
                        }
                        historyRateList.value =
                            historyRateItems.sortedByDescending { it.getDateValue() }
                        chartHistoryRatesList.value = ArrayList(historyRateItems)
                    }
                    is ResultState.Error -> {
                        showError(historyData.error)
                    }
                }
                when (otherRates) {
                    is ResultState.Success -> {
                        //Here we get the rates of all popular currencies
                        //along with from currency and then find the rate base on EUR rate
                        val currencyList = otherRates.data.currencyListWithRates
                        val currencyListWithRates = currencyList.map {
                            val convertedToAmount = CurrencyConverterUtil.getConvertedAmount(
                                fromCurrency,
                                it.code,
                                currencyList
                            )
                            it.apply {
                                rate = convertedToAmount
                            }
                        }
                        countryCurrencyRateItems.value = currencyListWithRates.filter {
                            it.code != fromCurrency
                        }
                    }
                    is ResultState.Error -> {
                        showError(otherRates.error)
                    }
                }
            }.collect {
                showLoading(false)
            }
        }
    }

}