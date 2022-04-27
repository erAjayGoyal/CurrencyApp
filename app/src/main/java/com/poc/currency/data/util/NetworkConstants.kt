package com.faris.data.util

import androidx.annotation.IntDef
import androidx.annotation.StringDef

object NetworkConstants {
    const val API_KEY = "b28c3a73c0f1739810195ff0b77828bf"
    @IntDef(
        NetworkErrorCodes.SERVICE_UNAVAILABLE,
        NetworkErrorCodes.MALFORMED_JSON,
        NetworkErrorCodes.NO_INTERNET,
        NetworkErrorCodes.UNEXPECTED_ERROR
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NetworkErrorCodes {
        companion object {
            const val SERVICE_UNAVAILABLE = 500
            const val MALFORMED_JSON = 501
            const val NO_INTERNET = 502
            const val UNEXPECTED_ERROR = 503
        }
    }

    @StringDef(
        NetworkErrorMessages.SERVICE_UNAVAILABLE,
        NetworkErrorMessages.MALFORMED_JSON,
        NetworkErrorMessages.NO_INTERNET,
        NetworkErrorMessages.UNEXPECTED_ERROR
    )

    @Retention(AnnotationRetention.SOURCE)
    annotation class NetworkErrorMessages {
        companion object {
            const val SERVICE_UNAVAILABLE =
                "System temporarily unavailable, please try again later."
            const val MALFORMED_JSON = "Oops! Unable to parse the response. Try again later"
            const val NO_INTERNET = "Oops! You are not connected to internet."
            const val UNEXPECTED_ERROR = "Something went wrong, please try again later."
        }
    }
}