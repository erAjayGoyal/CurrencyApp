package com.poc.currency.data.util

import android.text.format.DateFormat
import java.util.*

fun Date.serverFormattedDateString() = "${
    DateFormat.format(
        Constants.SERVER_DATE_FORMAT,
        this
    )
}"