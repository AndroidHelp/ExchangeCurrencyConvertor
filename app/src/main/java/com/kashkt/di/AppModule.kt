package com.kashkt.di

import android.content.Context
import androidx.room.Room
import com.kashkt.ui.main.data.datasource.local.LocalApiService
import com.kashkt.ui.main.data.datasource.local.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CurrencyDatabase::class.java,
            "currency_database"
        ).build()
    }

    @Provides
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase): LocalApiService {
        return currencyDatabase.apiService()
    }
}
