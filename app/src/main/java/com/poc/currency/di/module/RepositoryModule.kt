package com.poc.currency.di.module

import com.poc.currency.data.remote.api.CurrencyApi
import com.poc.currency.data.repository.CurrencyRepositoryImpl
import com.poc.currency.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCurrencyRepository(currencyApi: CurrencyApi): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyApi)
    }
}