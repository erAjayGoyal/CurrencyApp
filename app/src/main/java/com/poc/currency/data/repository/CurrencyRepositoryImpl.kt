package com.poc.currency.data.repository

import com.faris.data.util.NetworkConstants
import com.faris.data.util.serverFormattedDateString
import com.poc.currency.data.mapper.map
import com.poc.currency.data.remote.api.CurrencyApi
import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.repository.CurrencyRepository
import com.poc.currency.domain.response.ErrorEntity
import com.poc.currency.domain.response.currency.CurrencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val currencyApi: CurrencyApi) :
    BaseRepositoryImpl(),
    CurrencyRepository {
    override fun getSupportedCurrencies(): Flow<ResultState<CurrencyEntity.CurrencyList>> = flow {
        val result = apiCall { currencyApi.getCurrenciesSymbols(NetworkConstants.API_KEY).map() }
        if (result is ResultState.Success) {
            if (!result.data.isSuccess) {
                emit(
                    ResultState.Error(
                        ErrorEntity.Error(
                            errorCode = result.data.error?.errorCode,
                            errorMessage = result.data.error?.errorMessage
                        )
                    )
                )
            } else {
                emit(result)
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getCurrencyConversion(
        fromCurrency: String,
        toCurrency: List<String>
    ): Flow<ResultState<CurrencyEntity.CurrencyConversionResult>> = flow {
        val result = apiCall {
            currencyApi.convertLatest(
                NetworkConstants.API_KEY,
                from = fromCurrency,
                to = toCurrency.joinToString(",") { it }
            ).map()
        }
        if (result is ResultState.Success) {
            if (!result.data.isSuccess) {
                emit(
                    ResultState.Error(
                        ErrorEntity.Error(
                            errorCode = result.data.error?.errorCode,
                            errorMessage = result.data.error?.errorMessage
                        )
                    )
                )
            } else {
                emit(result)
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getCurrencyConversionByDays(
        days: Int,
        fromCurrency: String,
        toCurrency: List<String>
    ): Flow<ResultState<List<CurrencyEntity.CurrencyConversionResult>>> = flow {
        val start = Calendar.getInstance()
        start.add(Calendar.DATE, -days + 1)
        start.set(Calendar.HOUR, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        val end = Calendar.getInstance()

        val results = arrayListOf<CurrencyEntity.CurrencyConversionResult>()
        while (start.before(end)) {
            val result = apiCall {
                currencyApi.convert(
                    date = start.time.serverFormattedDateString(),
                    NetworkConstants.API_KEY,
                    from = fromCurrency,
                    to = toCurrency.joinToString(",") { it }
                )
            }
            if (result is ResultState.Success) {
                result.data.takeIf { it.success ?: false }?.map()?.let {
                    results.add(it)
                }
            }
            start.add(Calendar.DATE, 1)
        }
        if (results.isNotEmpty()) {
            emit(ResultState.Success(results.toList()))
        } else {
            emit(ResultState.Error(ErrorEntity.Error("No data", errorMessage = "No data")))
        }
    }.flowOn(Dispatchers.IO)
}