package com.poc.currency.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.response.ErrorEntity
import com.poc.currency.domain.response.currency.CurrencyEntity
import com.poc.currency.domain.usecases.CurrencyUseCase
import com.poc.currency.utils.Constants.BASE_CURRENCY
import com.poc.currency.utils.CurrencyConverterUtil
import com.poc.currency.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val currencyUseCase: CurrencyUseCase) :
    BaseViewModel() {

    private var _currencyList = MutableLiveData<List<CurrencyEntity.Currency>>()
    val currencyList: LiveData<List<CurrencyEntity.Currency>> = _currencyList

    private var _fromCurrency = MutableLiveData<CurrencyEntity.Currency?>()
    var fromCurrency: LiveData<CurrencyEntity.Currency?> = _fromCurrency

    private var _toCurrency = MutableLiveData<CurrencyEntity.Currency?>()
    val toCurrency: LiveData<CurrencyEntity.Currency?> = _toCurrency

    private var _fromAmount = MutableLiveData<String>()
    var fromAmount: LiveData<String> = _fromAmount
    private var _toAmount = MutableLiveData<String>()
    var toAmount: LiveData<String> = _toAmount

    val detailsScreenEvent = SingleLiveData<Unit>()
    val switchCurrency = SingleLiveData<Unit>()

    /**
     * Initial call in viewModel to fetch the Currency Symbol list and notify to UI
     */
    fun getCurrencyList() {
        showLoading(true)
        viewModelScope.launch {
            currencyUseCase.getSupportedCurrencies().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        showLoading(false)
                        _currencyList.value = result.data.currencyList
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        showError(result.error)
                    }
                }
            }
        }
    }

    /**
     * Method to set from currency
     */
    fun setFromCurrencyValue(currency: CurrencyEntity.Currency) {
        _fromCurrency.value = currency
    }

    /**
     * Method to set To currency
     */
    fun setToCurrencyValue(currency: CurrencyEntity.Currency) {
        _toCurrency.value = currency
    }

    /**
     * Method to convert amount based on selected currency in from & to dropdowns
     * @param amountString -> amount entered by user
     * @param isToAmountChanged -> flag to know whether amount is being changed or not
     */
    fun convert(amountString: String, isToAmountChanged: Boolean = false) {
        val fromCurrency: String? =
            if (isToAmountChanged) _toCurrency.value?.code else _fromCurrency.value?.code
        val toCurrency: String? =
            if (isToAmountChanged) _fromCurrency.value?.code else _toCurrency.value?.code

        if (fromCurrency == toCurrency
            || fromCurrency.isNullOrEmpty()
            || toCurrency.isNullOrEmpty()
        ) {
            resetAmount()
            return
        }

        val amount = amountString.toDoubleOrNull()
        if (amount == null) {
            showError(ErrorEntity.Error(errorCode = "INVALID", errorMessage = "Invalid amount"))
            return
        }
        convert(fromCurrency, toCurrency, amount, isToAmountChanged)
    }

    /**
     * Private function in viewmodel
     * accessing the usecase to fetch the
     * currency conversion rate
     * fromCurrency: Base currency selected
     * toCurrency: Currency to convert to
     * amount: Amount to be converted
     * isToAmount: Boolean to specify that selected amount is from To Amount Field
     */
    private fun convert(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        isToAmount: Boolean = false
    ) {
        showLoading(true)
        //Here we pass the base currency always
        //as EUR and the converted amount is calculated
        //based on its results
        viewModelScope.launch {
            currencyUseCase.getCurrencyConversion(
                BASE_CURRENCY,
                listOf(fromCurrency, toCurrency)
            ).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        showLoading(false)
                        val convertedAmount = CurrencyConverterUtil.getConvertedAmount(
                            fromCurrency,
                            toCurrency,
                            result.data.currencyListWithRates,
                            amount
                        )
                        if (null != convertedAmount) {
                            if (isToAmount) {
                                _fromAmount.value = convertedAmount.toString()
                            } else {
                                _toAmount.value = convertedAmount.toString()
                            }
                        } else {
                            showError(
                                ErrorEntity.Error(
                                    errorCode = "Error",
                                    errorMessage = "Something Went wrong"
                                )
                            )
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        showError(result.error)
                        clearAmounts()
                    }
                }
            }
        }
    }

    /**
     * Method to navigate user to Details screen
     */
    fun onDetailsClicked() {
        detailsScreenEvent.call()
    }

    /**
     * Method to set From amount
     */
    private fun setFromAmount(fromAmount: String) {
        _fromAmount.value = fromAmount
    }

    /**
     * Method to set To amount
     */
    private fun setToAmount(toAmount: String) {
        _toAmount.value = toAmount
    }

    /**
     * Clear the amount values
     */
    private fun clearAmounts() {
        setFromAmount("")
        setToAmount("")
    }

    private fun resetAmount() {
        setFromAmount("1")
        setToAmount("1")
    }

    /**
     * Method to invoke switching of currencies event
     */
    fun onSwitchCurrencyClicked() {
        switchCurrency.call()
    }


}