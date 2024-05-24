package com.kashkt.ui.main.data.repository

import com.kashkt.ui.main.data.datasource.local.LocalApiService
import com.kashkt.ui.main.data.datasource.remote.RemoteApiService
import com.kashkt.ui.main.data.model.currency.CurrencyResponse
import com.kashkt.ui.main.data.model.rates.Rates
import com.kashkt.ui.main.data.model.rates.RatesApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response
import java.math.BigDecimal

class CurrencyRepositoryImplTest {

    private lateinit var remoteApiService: RemoteApiService
    private lateinit var localApiService: LocalApiService
    private lateinit var currencyRepositoryImpl: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        remoteApiService = mock(RemoteApiService::class.java)
        localApiService = mock(LocalApiService::class.java)
        currencyRepositoryImpl = CurrencyRepositoryImpl(remoteApiService, localApiService)
    }

    @Test
    fun `fetch currency success`(): Unit = runBlocking {
        `when`(localApiService.getAllCurrencies()).thenReturn(listOf())
        `when`(remoteApiService.getCurrencies()).thenReturn(
            Response.success(
                CurrencyResponse(
                    AED = "Test",
                    AFN = "Test1"
                )
            )
        )
        assertEquals(
            "Test",
            currencyRepositoryImpl.getCurrencies().data?.find { it.currencyCode == "AED" }?.currencyName
        )
    }

    @Test
    fun `fetch currency failed`(): Unit = runBlocking {
        val errorBody =
            "{\"error\": true, \"status\": 400,\"message\": \"Failed to get currencies\",\"description\": \"Failed to get currencies\"}"
        `when`(remoteApiService.getCurrencies()).thenReturn(
            Response.error(
                400,
                errorBody.toResponseBody("application/json".toMediaTypeOrNull())
            )
        )
        assertEquals(
            "Failed to get currencies",
            currencyRepositoryImpl.getCurrencies().errorMessage
        )
    }

    @Test
    fun `fetch rates success`(): Unit = runBlocking {
        `when`(localApiService.getAllRates()).thenReturn(listOf())
        `when`(remoteApiService.getRates(anyString(), anyString())).thenReturn(
            Response.success(
                RatesApiResponse(
                    rates = Rates(AED = BigDecimal(3.5), AFN = BigDecimal(71.99))
                )
            )
        )
        assertEquals(
            "AED",
            currencyRepositoryImpl.getRates("USD", BigDecimal(1.0)).data?.get(0)?.currencyCode
        )
    }

    @Test
    fun `fetch rates failed`(): Unit = runBlocking {
        `when`(localApiService.getAllRates()).thenReturn(listOf())
        val errorBody =
            "{\"error\": true, \"status\": 400,\"message\": \"Failed to get rates\",\"description\": \"Failed to get rates\"}"
        `when`(remoteApiService.getRates(anyString(), anyString())).thenReturn(
            Response.error(
                400,
                errorBody.toResponseBody("application/json".toMediaTypeOrNull())
            )
        )
        assertEquals(
            "Failed to get rates",
            currencyRepositoryImpl.getRates("USD", BigDecimal(1.0)).errorMessage
        )
    }
}