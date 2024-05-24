package com.kashkt.ui.main.data.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("description") val description: String
)