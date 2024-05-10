package ru.alex0d.investapp.screens.stock.candles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.absoluteRelative
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CandlestickCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ChartValues
import com.patrykandpatrick.vico.core.cartesian.layer.CandlestickCartesianLayer
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.utils.toCurrencyFormat
import ru.alex0d.investapp.utils.toDecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CandlestickChart() {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                modelProducer.tryRunTransaction {
                    add(layerModel)
                    updateExtras { it[xToLongMapKey] = xToDate }
                }
                delay(5000)
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberCandlestickCartesianLayer(
                candles = CandlestickCartesianLayer.CandleProvider.absoluteRelative()
            ).apply {
                axisValueOverrider = object : AxisValueOverrider {
                    override fun getMaxY(minY: Float, maxY: Float, extraStore: ExtraStore) = maxY + 1
                    override fun getMinY(minY: Float, maxY: Float, extraStore: ExtraStore) = if (minY > 1) minY - 1 else minY
                }
            },
            startAxis = rememberStartAxis(
                valueFormatter = object : CartesianValueFormatter {
                    override fun format(value: Float, chartValues: ChartValues, verticalAxisPosition: AxisPosition.Vertical?): CharSequence {
                        return value.toDouble().toDecimalFormat()
                    }
                },
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = object : CartesianValueFormatter {
                    override fun format(value: Float, chartValues: ChartValues, verticalAxisPosition: AxisPosition.Vertical?): CharSequence {
                        val unix = chartValues.model.extraStore[xToLongMapKey][value] ?: return ""
                        return Instant.ofEpochSecond(unix).let {
                            val date = DateTimeFormatter.ofPattern("d MMM yy")
                                .withZone(ZoneId.systemDefault()).format(it)
                            return date
                        }
                    }
                },
                itemPlacer = AxisItemPlacer.Horizontal.default(
                    spacing = 15,
                    offset = 3,
                ),
            )
        ),
        modelProducer = modelProducer
    )
}

val x = listOf<Long>(1712620800, 1712707200, 1712793600, 1712880000, 1713139200, 1713225600, 1713312000, 1713398400, 1713484800, 1713744000, 1713830400, 1713916800, 1714003200, 1714089600, 1714176000, 1714348800, 1714435200, 1714608000, 1714694400, 1714953600, 1715040000, 1715126400)

val opening = listOf(308.23, 306.99, 307.55, 307.49, 306.9, 307.9, 309.0, 306.63, 307.92, 308.52, 315.94, 309.7, 308.01, 309.79, 310.01, 309.01, 309.51, 309.0, 307.5, 308.6, 306.27, 308.27)

val closing = listOf(306.78, 306.92, 307.1, 306.9, 307.87, 308.59, 306.65, 307.92, 308.19, 315.72, 308.88, 308.61, 309.5, 310.19, 309.6, 309.51, 308.87, 307.96, 307.7, 306.23, 308.27, 311.28)

val low = listOf(305.48, 304.82, 306.01, 305.41, 306.67, 307.06, 306.05, 305.53, 307.28, 308.0, 307.43, 307.7, 308.0, 309.01, 308.8, 308.1, 308.05, 306.73, 305.01, 305.76, 306.27, 308.26)

val high = listOf(309.28, 307.74, 308.33, 308.01, 308.42, 308.8, 309.89, 308.54, 308.48, 315.77, 317.22, 310.7, 309.8, 310.78, 310.98, 309.76, 310.0, 309.57, 309.1, 308.6, 308.95, 312.19)

val layerModel = CandlestickCartesianLayerModel.partial(
    x = x,
    opening = opening,
    closing = closing,
    low = low,
    high = high
)

val xToDate = x.associateBy { it.toFloat() }
val xToLongMapKey = ExtraStore.Key<Map<Float, Long>>()
