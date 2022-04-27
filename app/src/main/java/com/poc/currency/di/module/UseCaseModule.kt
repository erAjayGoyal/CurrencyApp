package com.poc.currency.di.module

import com.poc.currency.domain.repository.CurrencyRepository
import com.poc.currency.domain.usecases.CurrencyUseCase
import com.poc.currency.domain.usecases.CurrencyUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideCurrencyUseCase(repository: CurrencyRepository): CurrencyUseCase =
        CurrencyUseCaseImpl(repository)
}