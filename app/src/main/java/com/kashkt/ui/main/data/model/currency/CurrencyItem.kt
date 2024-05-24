package com.kashkt.ui.main.data.model.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyItem(
    @PrimaryKey
    val currencyCode: String,
    val currencyName: String
)
