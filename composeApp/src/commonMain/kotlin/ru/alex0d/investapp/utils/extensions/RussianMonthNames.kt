package ru.alex0d.investapp.utils.extensions

import kotlinx.datetime.format.MonthNames

val MonthNames.Companion.RUSSIAN_FULL: MonthNames
    get() = MonthNames(
        listOf(
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        )
    )

val MonthNames.Companion.RUSSIAN_ABBREVIATED: MonthNames
    get() = MonthNames(
        listOf(
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
            "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        )
    )
