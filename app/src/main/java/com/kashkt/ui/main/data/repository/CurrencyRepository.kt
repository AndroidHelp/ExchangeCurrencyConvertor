package com.kashkt.ui.main.data.repository

import com.kashkt.ui.main.data.model.BaseResponse
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem
import java.math.BigDecimal

interface CurrencyRepository {
    suspend fun getCurrencies(): BaseResponse<List<CurrencyItem>>
    suspend fun getRates(currencyCode: String, amount: BigDecimal): BaseResponse<List<RateItem>>
}