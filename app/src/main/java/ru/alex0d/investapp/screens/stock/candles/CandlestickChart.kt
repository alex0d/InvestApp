package ru.alex0d.investapp.screens.stock.candles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.absoluteRelative
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ChartValues
import com.patrykandpatrick.vico.core.cartesian.layer.CandlestickCartesianLayer
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import ru.alex0d.investapp.utils.toDecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CandlestickChart(modelProducer: CartesianChartModelProducer) {
    val marker = rememberMarker(showIndicator = false)
    val scroll = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
    val zoom = rememberVicoZoomState(initialZoom = remember { Zoom.min(Zoom.static(), Zoom.Content) })

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberCandlestickCartesianLayer(
                candles = CandlestickCartesianLayer.CandleProvider.absoluteRelative(),
                scaleCandleWicks = true,
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
                itemPlacer = AxisItemPlacer.Vertical.count({ 4 })
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = object : CartesianValueFormatter {
                    override fun format(value: Float, chartValues: ChartValues, verticalAxisPosition: AxisPosition.Vertical?): CharSequence {
                        Instant.ofEpochSecond(value.toLong()).let {
                            val date = DateTimeFormatter.ofPattern("d MMM yy")
                                .withZone(ZoneId.systemDefault()).format(it)
                            return date
                        }
                    }
                },
                itemPlacer = AxisItemPlacer.Horizontal.default(
                    spacing = 40,
                    offset = 3,
                ),
                guideline = null,
            ),
        ),
        marker = marker,
        scrollState = scroll,
        zoomState = zoom,
        modelProducer = modelProducer
    )
}
