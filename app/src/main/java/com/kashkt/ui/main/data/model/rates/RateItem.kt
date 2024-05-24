package com.kashkt.ui.main.data.model.rates

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
@Entity(tableName = "rates")
data class RateItem(
    @PrimaryKey
    val currencyCode: String,
    val currencyRate: BigDecimal?
)
