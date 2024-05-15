package ru.alex0d.investapp.data.mappers

import ru.alex0d.investapp.data.remote.models.CandleDto
import ru.alex0d.investapp.domain.models.Candle

fun CandleDto.toCandle(): Candle {
    return Candle(
        timestamp = timestamp,
        open = open,
        close = close,
        high = high,
        low = low,
        volume = volume
    )
}