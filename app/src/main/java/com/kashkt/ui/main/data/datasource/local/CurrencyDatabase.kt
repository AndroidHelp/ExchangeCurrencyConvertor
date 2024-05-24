package com.kashkt.ui.main.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem

@Database(entities = [CurrencyItem::class, RateItem::class], version = 1, exportSchema = false)
@TypeConverters(BigDecimalConverters::class)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun apiService(): LocalApiService
}