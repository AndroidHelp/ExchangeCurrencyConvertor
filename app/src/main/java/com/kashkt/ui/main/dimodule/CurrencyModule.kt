package com.kashkt.ui.main.dimodule

import com.kashkt.ui.main.data.datasource.local.LocalApiService
import com.kashkt.ui.main.data.datasource.remote.RemoteApiService
import com.kashkt.ui.main.data.repository.CurrencyRepository
import com.kashkt.ui.main.data.repository.CurrencyRepositoryImpl
import com.kashkt.ui.main.domain.CurrencyUseCase
import com.kashkt.ui.main.domain.CurrencyUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrencyModule {

    @Singleton
    @Provides
    fun provideRemoteApiService(
        remoteApiService: RemoteApiService,
        localApiService: LocalApiService
    ): CurrencyRepository =
        CurrencyRepositoryImpl(remoteApiService, localApiService)

    @Singleton
    @Provides
    fun provideRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyUseCase =
        CurrencyUseCaseImpl(currencyRepositoryImpl)

}