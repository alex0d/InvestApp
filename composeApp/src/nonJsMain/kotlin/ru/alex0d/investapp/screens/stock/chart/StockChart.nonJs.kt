package ru.alex0d.investapp.screens.stock.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.Scroll
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.CandlestickCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.absoluteRelative
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.data.ExtraStore
import ru.alex0d.investapp.screens.stock.chart.vico.VicoChartModel
import ru.alex0d.investapp.screens.stock.chart.vico.rememberMarker
import ru.alex0d.investapp.utils.DateTimeUtils

@Composable
actual fun StockChart(chartModel: ChartModel) {
    val modelProducer = (chartModel as VicoChartModel).modelProducer
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
                valueFormatter = { _, value, _ ->
                    DateTimeUtils.russianAbbreviatedDate(epochSeconds = value.toLong())
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
