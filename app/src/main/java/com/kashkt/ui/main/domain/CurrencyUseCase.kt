package com.kashkt.ui.main.domain

import com.kashkt.ui.main.data.model.BaseResponse
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem
import java.math.BigDecimal

interface CurrencyUseCase {
    suspend fun getCurrency(): BaseResponse<List<CurrencyItem>>
    suspend fun getRates(currencyCode: String, amount: BigDecimal): BaseResponse<List<RateItem>>

}