package com.kashkt.ui.main.domain

import com.kashkt.ui.main.data.model.BaseResponse
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem
import com.kashkt.ui.main.data.repository.CurrencyRepository
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyUseCaseImpl @Inject constructor(private val currencyRepository: CurrencyRepository) :
    CurrencyUseCase {
    override suspend fun getCurrency(): BaseResponse<List<CurrencyItem>> {
        return currencyRepository.getCurrencies()
    }

    override suspend fun getRates(
        currencyCode: String,
        amount: BigDecimal
    ): BaseResponse<List<RateItem>> {
        return currencyRepository.getRates(currencyCode, amount)
    }
}