package ru.alex0d.investapp.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.alex0d.investapp.utils.extensions.RUSSIAN_ABBREVIATED

object DateTimeUtils {
    fun russianAbbreviatedDate(epochSeconds: Long): String {
        Instant
            .fromEpochSeconds(epochSeconds)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .let { localDateTime ->
                val date = LocalDate.Format {
                    dayOfMonth()
                    char(' ')
                    monthName(MonthNames.RUSSIAN_ABBREVIATED)
                    char(' ')
                    yearTwoDigits(baseYear = 1970)
                }
                return date.format(localDateTime)
            }
    }
}