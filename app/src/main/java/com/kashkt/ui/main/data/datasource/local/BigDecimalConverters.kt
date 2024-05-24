package com.kashkt.ui.main.data.datasource.local

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}
