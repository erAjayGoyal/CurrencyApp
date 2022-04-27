package com.poc.currency.data.remote.api

import com.poc.currency.data.remote.dto.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApi {

    @GET("symbols")
    suspend fun getCurrenciesSymbols(@Query("access_key") apiKey: String): CurrencyDto.CurrencyList

    @GET("{date}")
    suspend fun convert(
        @Path("date") date: String,
        @Query("access_key") apiKey: String,
        @Query("base") from: String,
        @Query("symbols") to: String,
    ): CurrencyDto.ConversionResultDto

    @GET("latest")
    suspend fun convertLatest(
        @Query("access_key") apiKey: String,
        @Query("base") from: String,
        @Query("symbols") to: String,
    ): CurrencyDto.ConversionResultDto
}