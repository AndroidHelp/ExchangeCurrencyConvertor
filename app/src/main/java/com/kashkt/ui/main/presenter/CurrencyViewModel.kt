package com.kashkt.ui.main.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashkt.R
import com.kashkt.ui.main.data.model.currency.CurrencyItem
import com.kashkt.ui.main.data.model.rates.RateItem
import com.kashkt.ui.main.domain.CurrencyUseCase
import com.kashkt.ui.main.presenter.MainActivity.Companion.DEFAULT_CURRENCY
import com.kashkt.ui.main.presenter.MainActivity.Companion.DEFAULT_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyUseCase: CurrencyUseCase
) : ViewModel() {

    private val _currencyData = MutableStateFlow(listOf<CurrencyItem>())
    val currencyData: StateFlow<List<CurrencyItem>> = _currencyData

    private val _ratesData = MutableStateFlow(listOf<RateItem>())
    val ratesData: StateFlow<List<RateItem>> = _ratesData

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow(Any())
    val error: StateFlow<Any> = _error

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline

    private val _inputAmountCurrency: MutableStateFlow<Pair<String, String>> =
        MutableStateFlow(Pair(DEFAULT_CURRENCY, DEFAULT_VALUE))
    private val inputAmountCurrency: StateFlow<Pair<String, String>> = _inputAmountCurrency

    init {
        getCurrencies()
        viewModelScope.launch {
            inputAmountCurrency.debounce(500).collectLatest { input ->
                if (input.second.isNotEmpty()) {
                    getRates(input.first, BigDecimal(input.second))
                }
            }
        }
    }

    fun onTextChanged(currencyCode: String, amount: String) {
        _inputAmountCurrency.update { Pair(currencyCode, amount) }
    }

    private fun getCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _loading.value = true
                currencyUseCase.getCurrency()
            }.onSuccess {
                it.data?.let { currencies ->
                    _currencyData.value = currencies
                }
                it.errorMessage?.let { message ->
                    _error.value = message
                    _currencyData.value = listOf()
                }
                _isOffline.value = !it.isOnline
                _loading.value = false
            }.onFailure {
                _loading.value = false
                _error.value = it.message ?: R.string.something_went_wrong
            }
        }
    }

    fun getRates(currencyCode: String, amount: BigDecimal) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _loading.value = true
                currencyUseCase.getRates(currencyCode, amount)
            }.onSuccess {
                it.data?.let { rates ->
                    _ratesData.value = rates
                }
                it.errorMessage?.let { message ->
                    _error.value = message
                    _ratesData.value = listOf()
                }
                _isOffline.value = !it.isOnline
                _loading.value = false
            }.onFailure {
                _loading.value = false
                _error.value = it.message ?: R.string.something_went_wrong
            }
        }
    }
}