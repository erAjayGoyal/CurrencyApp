package com.poc.currency.data.repository

import android.util.Log
import android.util.MalformedJsonException
import com.faris.data.util.NetworkConstants
import com.google.gson.GsonBuilder
import com.poc.currency.data.remote.dto.ErrorDto
import com.poc.currency.domain.common.ResultState
import com.poc.currency.domain.response.ErrorEntity
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.lang.Exception
import java.net.SocketException
import java.net.SocketTimeoutException

abstract class BaseRepositoryImpl {
    private val logFormatter: String = "%s | %s"
    protected suspend fun <T : Any> apiCall(call: suspend () -> T): ResultState<T> {
        return try {
            val response = call()
            ResultState.Success(response)
        } catch (e: Throwable) {
            ResultState.Error(handleError(e))
        }
    }

    private fun handleError(throwable: Throwable): ErrorEntity.Error {
        return when (throwable) {
            is SocketTimeoutException, is SocketException, is InterruptedIOException -> {
                ErrorEntity.Error(
                    NetworkConstants.NetworkErrorCodes.SERVICE_UNAVAILABLE,
                    NetworkConstants.NetworkErrorMessages.SERVICE_UNAVAILABLE
                )
            }
            is MalformedJsonException -> {
                ErrorEntity.Error(
                    NetworkConstants.NetworkErrorCodes.MALFORMED_JSON,
                    NetworkConstants.NetworkErrorMessages.MALFORMED_JSON
                )
            }
            is IOException -> {
                ErrorEntity.Error(
                    NetworkConstants.NetworkErrorCodes.NO_INTERNET,
                    NetworkConstants.NetworkErrorMessages.NO_INTERNET
                )
            }
            is HttpException -> {
                val errorResponse: ErrorDto.ErrorResponse? =
                    getError(throwable.response()?.errorBody())

                if (errorResponse?.error == null) {
                    ErrorEntity.Error(
                        NetworkConstants.NetworkErrorCodes.UNEXPECTED_ERROR,
                        NetworkConstants.NetworkErrorMessages.UNEXPECTED_ERROR
                    )
                } else {
                    ErrorEntity.Error(
                        errorResponse.error.code,
                        errorResponse.error.message.toString()
                    )
                }
                ErrorEntity.Error(
                    NetworkConstants.NetworkErrorCodes.NO_INTERNET,
                    NetworkConstants.NetworkErrorMessages.NO_INTERNET
                )
            }
            else -> {
                ErrorEntity.Error(
                    NetworkConstants.NetworkErrorCodes.UNEXPECTED_ERROR,
                    NetworkConstants.NetworkErrorMessages.UNEXPECTED_ERROR
                )
            }
        }
    }

    private fun getError(errorBody: ResponseBody?): ErrorDto.ErrorResponse? {
        return try {
            val response = GsonBuilder().create()
                .fromJson(errorBody?.string(), ErrorDto.ErrorResponse::class.java)
            Log.e("Currency API error: %s", response?.toString()!!)
            response
        } catch (e: Exception) {
            ErrorDto.ErrorResponse(
                isSuccess = false,
                error = ErrorDto.Error(
                    NetworkConstants.NetworkErrorCodes.UNEXPECTED_ERROR,
                    NetworkConstants.NetworkErrorMessages.UNEXPECTED_ERROR
                )
            )
        }
    }
}