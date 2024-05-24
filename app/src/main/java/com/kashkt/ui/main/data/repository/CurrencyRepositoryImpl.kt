package com.kashkt.ui.main.data.repository

import com.google.gson.Gson
import com.kashkt.ui.main.data.datasource.local.LocalApiService
import com.kashkt.ui.main.data.datasource.remote.RemoteApiService
import com.kashkt.ui.main.data.model.BaseResponse
import com.kashkt.ui.main.data.model.ErrorResponse
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteApiService: RemoteApiService,
    private val localApiService: LocalApiService
) : CurrencyRepository {
    override suspend fun getCurrencies(): BaseResponse<List<CurrencyItem>> {
        try {
            val response = remoteApiService.getCurrencies()
            return if (response.isSuccessful) {
                val currencyData = response.body()?.toList()
                currencyData?.let {
                    localApiService.insertCurrencies(it)
                }
                BaseResponse(code = response.code(), data = currencyData)
            } else {
                val errorResponse = response.errorBody()?.string()
                val error = Gson().fromJson(errorResponse, ErrorResponse::class.java)
                BaseResponse(code = response.code(), errorMessage = error.description)
            }
        } catch (e: Exception) {
            return when {
                e.message == null -> BaseResponse(
                    code = 0,
                    errorMessage = "Something went wrong!, try again later!"
                )

                e.message?.contains("Unable to resolve host") == true && localApiService.getAllCurrencies()
                    .isNotEmpty() ->
                    BaseResponse(
                        code = 200,
                        data = localApiService.getAllCurrencies(),
                        isOnline = false
                    )

                else -> BaseResponse(code = 0, errorMessage = e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun getRates(
        currencyCode: String,
        amount: BigDecimal
    ): BaseResponse<List<RateItem>> {
        try {
            val response = remoteApiService.getRates(base = currencyCode)
            return if (response.isSuccessful) {
                val ratesData = response.body()?.rates?.toList(BigDecimal(1))
                ratesData?.let {
                    localApiService.insertRates(ratesData)
                }
                BaseResponse(code = response.code(), data = response.body()?.rates?.toList(amount))
            } else {
                val errorResponse = response.errorBody()?.string()
                val error = Gson().fromJson(errorResponse, ErrorResponse::class.java)
                BaseResponse(code = response.code(), errorMessage = error.description)
            }
        } catch (e: Exception) {
            return when {
                e.message == null -> BaseResponse(
                    code = 0,
                    errorMessage = "Something went wrong!, try again later!"
                )

                e.message?.contains("Unable to resolve host") == true && localApiService.getAllRates()
                    .isNotEmpty() ->
                    BaseResponse(
                        code = 200,
                        data = calculateRates(currencyCode, amount, localApiService.getAllRates()),
                        isOnline = false
                    )

                else -> BaseResponse(code = 0, errorMessage = e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun calculateRates(
        selectedCurrency: String,
        amount: BigDecimal,
        allRates: List<RateItem>
    ): List<RateItem> {
        val selectedCurrencyRate =
            allRates.firstOrNull { it.currencyCode == selectedCurrency }?.currencyRate
                ?: return emptyList()
        return allRates.map { rate ->
            val convertedRate = rate.currencyRate?.multiply(amount)?.div(selectedCurrencyRate)
            RateItem(rate.currencyCode, convertedRate)
        }
    }
}