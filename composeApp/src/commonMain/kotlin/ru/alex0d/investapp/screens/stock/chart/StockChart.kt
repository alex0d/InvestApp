package ru.alex0d.investapp.screens.stock.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.Scroll
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.CandlestickCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.absoluteRelative
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.data.ExtraStore
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import ru.alex0d.investapp.utils.extensions.RUSSIAN_ABBREVIATED

@Composable
fun StockChart(modelProducer: CartesianChartModelProducer) {
    val marker = rememberMarker(showIndicator = false)
    val scroll = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
    val zoom = rememberVicoZoomState(initialZoom = remember { Zoom.min(Zoom.fixed(1f), Zoom.Content) })

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                rangeProvider = object : CartesianLayerRangeProvider {
                    override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                        maxY + 1

                    override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                        if (minY > 1) minY - 1 else minY
                }
            ),

            rememberCandlestickCartesianLayer(
                candleProvider = CandlestickCartesianLayer.CandleProvider.absoluteRelative(),
                scaleCandleWicks = true,
                rangeProvider = object : CartesianLayerRangeProvider {
                    override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                        maxY + 1

                    override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                        if (minY > 1) minY - 1 else minY
                }
            ),

            startAxis = VerticalAxis.rememberStart(
                itemPlacer = remember { VerticalAxis.ItemPlacer.step(step = { 4.0 }) }
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = object : CartesianValueFormatter {
                    override fun format(
                        context: CartesianMeasuringContext,
                        value: Double,
                        verticalAxisPosition: Axis.Position.Vertical?,
                    ): CharSequence {
                        Instant
                            .fromEpochSeconds(value.toLong())
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
                },
                itemPlacer = remember {
                    HorizontalAxis.ItemPlacer.aligned(spacing = { 40 }, offset = { 3 })
                },
                guideline = null,
            ),
            marker = marker,
        ),
        scrollState = scroll,
        zoomState = zoom,
        modelProducer = modelProducer,
    )
}
