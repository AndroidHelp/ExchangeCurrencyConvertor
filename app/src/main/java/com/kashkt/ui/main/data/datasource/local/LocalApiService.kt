package com.kashkt.ui.main.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem

@Dao
interface LocalApiService {
    @Query("SELECT * FROM currency")
    suspend fun getAllCurrencies(): List<CurrencyItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyItem>)

    @Query("SELECT * FROM rates")
    suspend fun getAllRates(): List<RateItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<RateItem>)
}