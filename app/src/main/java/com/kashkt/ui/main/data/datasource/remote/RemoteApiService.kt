package com.kashkt.ui.main.data.datasource.remote

import com.kashkt.BuildConfig
import com.kashkt.ui.main.data.model.currency.CurrencyResponse
import com.kashkt.ui.main.data.model.rates.RatesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApiService {

    @GET("currencies.json")
    suspend fun getCurrencies(): Response<CurrencyResponse>

    @GET("latest.json")
    suspend fun getRates(
        @Query("base") base: String = "USD",
        @Query("app_id") appId: String = BuildConfig.API_TOKEN
    ): Response<RatesApiResponse>
}