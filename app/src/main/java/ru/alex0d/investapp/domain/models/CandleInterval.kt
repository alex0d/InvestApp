package ru.alex0d.investapp.domain.models

import androidx.annotation.StringRes
import ru.alex0d.investapp.R

enum class CandleInterval {
    INTERVAL_1_MIN,
    INTERVAL_2_MIN,
    INTERVAL_3_MIN,
    INTERVAL_5_MIN,
    INTERVAL_10_MIN,
    INTERVAL_15_MIN,
    INTERVAL_30_MIN,
    INTERVAL_HOUR,
    INTERVAL_2_HOUR,
    INTERVAL_4_HOUR,
    INTERVAL_DAY,
    INTERVAL_WEEK,
    INTERVAL_MONTH,
    INTERVAL_UNSPECIFIED
}

@StringRes
fun CandleInterval.toStringRes(): Int {
    return when (this) {
        CandleInterval.INTERVAL_1_MIN -> R.string.interval_1_min
        CandleInterval.INTERVAL_2_MIN -> R.string.interval_2_min
        CandleInterval.INTERVAL_3_MIN -> R.string.interval_3_min
        CandleInterval.INTERVAL_5_MIN -> R.string.interval_5_min
        CandleInterval.INTERVAL_10_MIN -> R.string.interval_10_min
        CandleInterval.INTERVAL_15_MIN -> R.string.interval_15_min
        CandleInterval.INTERVAL_30_MIN -> R.string.interval_30_min
        CandleInterval.INTERVAL_HOUR -> R.string.interval_hour
        CandleInterval.INTERVAL_2_HOUR -> R.string.interval_2_hour
        CandleInterval.INTERVAL_4_HOUR -> R.string.interval_4_hour
        CandleInterval.INTERVAL_DAY -> R.string.interval_day
        CandleInterval.INTERVAL_WEEK -> R.string.interval_week
        CandleInterval.INTERVAL_MONTH -> R.string.interval_month
        CandleInterval.INTERVAL_UNSPECIFIED -> throw IllegalArgumentException("Interval not specified")
    }
}

fun CandleInterval.toApiString(): String {
    return when (this) {
        CandleInterval.INTERVAL_1_MIN -> "1min"
        CandleInterval.INTERVAL_2_MIN -> "2min"
        CandleInterval.INTERVAL_3_MIN -> "3min"
        CandleInterval.INTERVAL_5_MIN -> "5min"
        CandleInterval.INTERVAL_10_MIN -> "10min"
        CandleInterval.INTERVAL_15_MIN -> "15min"
        CandleInterval.INTERVAL_30_MIN -> "30min"
        CandleInterval.INTERVAL_HOUR -> "hour"
        CandleInterval.INTERVAL_2_HOUR -> "2hour"
        CandleInterval.INTERVAL_4_HOUR -> "4hour"
        CandleInterval.INTERVAL_DAY -> "day"
        CandleInterval.INTERVAL_WEEK -> "week"
        CandleInterval.INTERVAL_MONTH -> "month"
        CandleInterval.INTERVAL_UNSPECIFIED -> throw IllegalArgumentException("Interval not specified")
    }
}