package com.poc.currency.ui.fragment

import com.google.common.truth.Truth
import com.poc.currency.BaseViewModelTest
import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.response.ErrorEntity
import com.poc.currency.domain.response.currency.CurrencyEntity
import com.poc.currency.domain.usecases.CurrencyUseCase
import com.poc.currency.getOrAwaitValueTest
import com.poc.currency.runBlockingMainTest
import com.poc.currency.ui.vm.CurrencyConverterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyConverterFragmentTest : BaseViewModelTest() {

    @Mock
    private lateinit var currencyUseCase: CurrencyUseCase

    private lateinit var currencySymbolViewModel: CurrencyConverterViewModel

    @Before
    fun setUp() {
        currencySymbolViewModel = CurrencyConverterViewModel(currencyUseCase)
    }

    @Test
    fun `getCurrencySymbolList should return success`() = runBlockingMainTest {

        //GIVEN
        val flowCurrencies = flowOf(ResultState.Success(getTestCurrenciesSymbols()))

        //WHEN
        Mockito.doReturn(flowCurrencies).`when`(currencyUseCase).getSupportedCurrencies()

        currencySymbolViewModel.getCurrencyList()
        val currenciesList = currencySymbolViewModel.currencyList.getOrAwaitValueTest()
        //THEN
        Truth.assertThat(currenciesList).isNotEmpty()
    }

    @Test
    fun `getCurrencySymbolList should return exception`() = runBlockingMainTest {
        //GIVEN
        val flowCurrenciesError = flowOf(getErrorCurrencyList())
        //WHEN
        Mockito.doReturn(flowCurrenciesError).`when`(currencyUseCase).getSupportedCurrencies()
        currencySymbolViewModel.getCurrencyList()
        val error = currencySymbolViewModel.errorEvent.getOrAwaitValueTest()
        //THEN
        Truth.assertThat(error?.errorCode).isNotNull()
    }

    @Test
    fun `triggers details click event`() = runBlockingMainTest {
        var navigated = false
        currencySymbolViewModel.onDetailsClicked()
        currencySymbolViewModel.detailsScreenEvent.getOrAwaitValueTest {
            navigated = true
        }
        Truth.assertThat(navigated).isTrue()
    }


    private fun getErrorCurrencyList(): ResultState<CurrencyEntity.CurrencyList> {
        return ResultState.Error(getTestError())
    }

    private fun getTestError(): ErrorEntity.Error {
        return ErrorEntity.Error(
            errorCode = "FAILED",
            errorMessage = "Failed to get Response"
        )
    }

    private fun getTestCurrenciesSymbols(): CurrencyEntity.CurrencyList {
        return CurrencyEntity.CurrencyList(
            currencyList = listOf(
                CurrencyEntity.Currency(
                    code = "AED",
                    value = "United Arab Dirham",
                ), CurrencyEntity.Currency(
                    code = "USD",
                    value = "United States Dollar",
                ), CurrencyEntity.Currency(
                    code = "EUR",
                    value = "Euro",
                )
            )
        )
    }

}