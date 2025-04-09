package ru.alex0d.investapp.domain.models

import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.interval_10_min
import investapp.composeapp.generated.resources.interval_15_min
import investapp.composeapp.generated.resources.interval_1_min
import investapp.composeapp.generated.resources.interval_2_hour
import investapp.composeapp.generated.resources.interval_2_min
import investapp.composeapp.generated.resources.interval_30_min
import investapp.composeapp.generated.resources.interval_3_min
import investapp.composeapp.generated.resources.interval_4_hour
import investapp.composeapp.generated.resources.interval_5_min
import investapp.composeapp.generated.resources.interval_day
import investapp.composeapp.generated.resources.interval_hour
import investapp.composeapp.generated.resources.interval_month
import investapp.composeapp.generated.resources.interval_week
import org.jetbrains.compose.resources.StringResource

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

fun CandleInterval.toStringRes(): StringResource {
    return when (this) {
        CandleInterval.INTERVAL_1_MIN -> Res.string.interval_1_min
        CandleInterval.INTERVAL_2_MIN -> Res.string.interval_2_min
        CandleInterval.INTERVAL_3_MIN -> Res.string.interval_3_min
        CandleInterval.INTERVAL_5_MIN -> Res.string.interval_5_min
        CandleInterval.INTERVAL_10_MIN -> Res.string.interval_10_min
        CandleInterval.INTERVAL_15_MIN -> Res.string.interval_15_min
        CandleInterval.INTERVAL_30_MIN -> Res.string.interval_30_min
        CandleInterval.INTERVAL_HOUR -> Res.string.interval_hour
        CandleInterval.INTERVAL_2_HOUR -> Res.string.interval_2_hour
        CandleInterval.INTERVAL_4_HOUR -> Res.string.interval_4_hour
        CandleInterval.INTERVAL_DAY -> Res.string.interval_day
        CandleInterval.INTERVAL_WEEK -> Res.string.interval_week
        CandleInterval.INTERVAL_MONTH -> Res.string.interval_month
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