package com.poc.currency.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlin.Boolean
import kotlin.Int
import kotlin.String

sealed class ErrorDto {
    data class ErrorResponse(
        @SerializedName("success")
        val isSuccess: Boolean? = false,
        @SerializedName("error")
        val error: Error? = null
    )

    data class Error(
        @SerializedName("code")
        val code: Int? = null,
        @SerializedName("info")
        val message: String? = "",
        @SerializedName("type")
        val type: String? = ""
    )
}