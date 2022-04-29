package com.poc.currency.ui.fragment

import com.google.common.truth.Truth
import com.poc.currency.BaseViewModelTest
import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.response.currency.CurrencyEntity
import com.poc.currency.domain.usecases.CurrencyUseCase
import com.poc.currency.getOrAwaitValueTest
import com.poc.currency.runBlockingMainTest
import com.poc.currency.ui.vm.CurrencyDetailsViewModel
import com.poc.currency.utils.Constants
import com.poc.currency.utils.Constants.BASE_CURRENCY
import com.poc.currency.utils.Constants.HISTORY_DATE_SIZE
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
class CurrencyDetailsViewModelTest : BaseViewModelTest() {
    @Mock
    private lateinit var currencyUseCase: CurrencyUseCase

    private lateinit var detailsViewModel: CurrencyDetailsViewModel

    @Before
    fun setup() {
        detailsViewModel = CurrencyDetailsViewModel(currencyUseCase)
    }

    @Test
    fun `getCurrencyHistory should return success`() = runBlockingMainTest {
        val fromCurrency = "AED"
        val toCurrency = "USD"
        //GIVEN
        val flowCurrencies =
            flowOf(ResultState.Success(getTestConversionHistoryResult()))
        val currenciesList =
            flowOf(ResultState.Success(getTestConversionResult()))

        //WHEN
        Mockito.doReturn(flowCurrencies).`when`(currencyUseCase)
            .getCurrencyConversionByDays(
                HISTORY_DATE_SIZE,
                BASE_CURRENCY,
                listOf(fromCurrency, toCurrency)
            )
        Mockito.doReturn(currenciesList).`when`(currencyUseCase)
            .getCurrencyConversion(
                BASE_CURRENCY,
                getFavCurrencyListTestData()
            )

        detailsViewModel.getCurrencyHistoryData(fromCurrency, toCurrency)
        val rateHistoryList = detailsViewModel.historyRateList.value
        val otherRatesList = detailsViewModel.countryCurrencyRateItems.value
        //THEN
        Truth.assertThat(rateHistoryList?.size).isGreaterThan(0)
        Truth.assertThat(otherRatesList?.size).isGreaterThan(0)
    }

    @Test
    fun `getCountryCurrency should populate chart list`() =
        runBlockingMainTest {
            val fromCurrency = "AED"
            val toCurrency = "USD"
            //GIVEN
            val flowCurrencies = flowOf(
                ResultState.Success(
                    getTestConversionHistoryResult()
                )
            )
            val currenciesList =
                flowOf(ResultState.Success(getTestConversionResult()))
            //WHEN
            Mockito.doReturn(flowCurrencies).`when`(currencyUseCase)
                .getCurrencyConversionByDays(
                    HISTORY_DATE_SIZE,
                    BASE_CURRENCY,
                    listOf(fromCurrency, toCurrency)
                )
            Mockito.doReturn(currenciesList).`when`(currencyUseCase)
                .getCurrencyConversion(
                    BASE_CURRENCY,
                    getFavCurrencyListTestData()
                )

            detailsViewModel.getCurrencyHistoryData(fromCurrency, toCurrency)
            val chartItems = detailsViewModel.chartHistoryRatesList.getOrAwaitValueTest()
            //THEN
            Truth.assertThat(chartItems.size).isGreaterThan(0)
        }


    private fun getFavCurrencyListTestData(
    ): ArrayList<String> {
        val popularList = Constants.getPopularCountryCurrencies()
        popularList.add("AED")
        popularList.add("USD")
        return popularList
    }

    private fun getTestConversionResult(): CurrencyEntity.CurrencyConversionResult {
        return CurrencyEntity.CurrencyConversionResult(
            fromCurrency = "EUR",
            currencyListWithRates = listOf(
                CurrencyEntity.Currency(code = "AED", value = "AED", rate = 12.5),
                CurrencyEntity.Currency(code = "USD", value = "USD", rate = 0.5),
            ),
            dateString = "2022-06-02"
        )
    }

    private fun getTestConversionHistoryResult(): List<CurrencyEntity.CurrencyConversionResult> {
        return listOf(
            CurrencyEntity.CurrencyConversionResult(
                fromCurrency = "EUR",
                currencyListWithRates = listOf(
                    CurrencyEntity.Currency(code = "USD", value = "USD", rate = 12.5),
                    CurrencyEntity.Currency(code = "AED", value = "AED", rate = 10.5),
                ),
                dateString = "2022-06-02"
            ),
            CurrencyEntity.CurrencyConversionResult(
                fromCurrency = "EUR",
                currencyListWithRates = listOf(
                    CurrencyEntity.Currency(code = "USD", value = "USD", rate = 12.51),
                    CurrencyEntity.Currency(code = "AED", value = "AED", rate = 10.49),
                ),
                dateString = "2022-06-01"
            )
        )
    }
}