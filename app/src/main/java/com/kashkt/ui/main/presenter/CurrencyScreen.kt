package com.kashkt.ui.main.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kashkt.R
import com.kashkt.ui.main.data.model.rates.RateItem
import com.kashkt.ui.main.presenter.MainActivity.Companion.DEFAULT_CURRENCY
import com.kashkt.ui.main.presenter.MainActivity.Companion.DEFAULT_VALUE
import java.math.BigDecimal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(currencyViewModel: CurrencyViewModel) {
    val currencyData by currencyViewModel.currencyData.collectAsState()
    val rates by currencyViewModel.ratesData.collectAsState()
    val loading by currencyViewModel.loading.collectAsState()
    val error by currencyViewModel.error.collectAsState()
    val offline by currencyViewModel.isOffline.collectAsState()

    var amount by remember {
        mutableStateOf(TextFieldValue(DEFAULT_VALUE))
    }
    var expanded by remember { mutableStateOf(false) }

    var selectedCurrency by remember {
        mutableStateOf(
            Pair(
                DEFAULT_CURRENCY,
                currencyData.find { it.currencyCode == DEFAULT_CURRENCY }?.currencyName
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 56.dp, start = 24.dp, end = 24.dp)
    ) {
        if (offline){
            Text(text = stringResource(id = R.string.offline_message), modifier = Modifier.fillMaxWidth().background(
                Color.Yellow).padding(16.dp))
        }
        TextField(
            value = amount,
            onValueChange = {
                amount = it
                currencyViewModel.onTextChanged(
                    currencyCode = selectedCurrency.first,
                    amount = it.text
                )
            },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            label = { Text(stringResource(id = R.string.enter_amount)) }
        )

        Text(
            text = stringResource(id = R.string.select_currency),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.padding(top = 2.dp),
            onExpandedChange = {
                expanded = it
            }
        ) {
            currencyData.find { it.currencyCode == selectedCurrency.first }?.currencyName?.let {
                TextField(
                    value = it,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = TextFieldDefaults.colors(disabledTextColor = Color.DarkGray)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                currencyData.forEach { (key, value) ->
                    DropdownMenuItem(
                        text = { Text(text = value) },
                        onClick = {
                            selectedCurrency = Pair(key, amount.text)
                            expanded = false
                            currencyViewModel.getRates(key, BigDecimal(amount.text))
                        }
                    )
                }
            }
        }
        if (loading) {
            LoadingIndicator()
        } else {
            if (rates.isNotEmpty()) {
                RatesList(rates)
            }
            ErrorMessage(error)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: Any) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (message is Int) {
            Text(text = stringResource(id = message))
        } else if (message is String) {
            Text(text = message)
        }
    }
}

@Composable
fun RatesList(rates: List<RateItem>) {
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        items(rates.size) { index ->
            val rate = rates[index]
            CurrencyItem(rate)
        }
    }
}

@Composable
fun CurrencyItem(rate: RateItem) {
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = rate.currencyCode, style = TextStyle(fontSize = 18.sp))
            Text(
                text = "${rate.currencyRate}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
