package com.kashkt.ui.main.data.model

data class BaseResponse<T>(
    val code: Int,
    val errorMessage: String? = null,
    val data: T? = null,
    val isOnline: Boolean = true
)