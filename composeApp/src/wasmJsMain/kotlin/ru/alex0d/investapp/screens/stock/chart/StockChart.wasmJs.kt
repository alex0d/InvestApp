package ru.alex0d.investapp.screens.stock.chart

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import ru.alex0d.investapp.screens.stock.chart.linear.LinearChartModel
import ru.alex0d.investapp.utils.DateTimeUtils

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
actual fun StockChart(chartModel: ChartModel) {
    val linearChartModel = chartModel as LinearChartModel

    if (linearChartModel.timestamps.isEmpty() || linearChartModel.values.isEmpty()) {
        return
    }

    val dataPoints = linearChartModel.timestamps.zip(linearChartModel.values) { time, value ->
        DefaultPoint(time.toFloat(), value.toFloat())
    }

    val minY = linearChartModel.values.minOrNull()?.toFloat()?.times(0.98f) ?: 0f
    val maxY = linearChartModel.values.maxOrNull()?.toFloat()?.times(1.02f) ?: 100f

    val primaryColor = MaterialTheme.colorScheme.primary

    ChartLayout(
        modifier = Modifier.padding(8.dp),
    ) {
        XYGraph(
            xAxisModel = FloatLinearAxisModel(
                range = dataPoints.minOf { it.x }..dataPoints.maxOf { it.x },
                minimumMajorTickSpacing = 55.dp,
                minorTickCount = 0,
            ),
            yAxisModel = FloatLinearAxisModel(
                range = minY..maxY,
                minimumMajorTickSpacing = 30.dp,
                minorTickCount = 0,
                allowZooming = false,
                allowPanning = false,
            ),
            xAxisLabels = { timestamp: Float ->
                DateTimeUtils.russianAbbreviatedDate(epochSeconds = timestamp.toLong())
            },
            zoomEnabled = true,
            allowIndependentZoom = true,
            panEnabled = true
        ) {
            LinePlot(
                data = dataPoints,
                lineStyle = LineStyle(
                    brush = SolidColor(primaryColor),
                    strokeWidth = 2.dp
                ),
                symbol = { _ ->
                    Symbol(
                        shape = CircleShape,
                        fillBrush = SolidColor(primaryColor),
                        modifier = Modifier.size(2.dp),
                        outlineBrush = SolidColor(primaryColor.copy(alpha = 0.5f)),
                    )
                }
            )
        }
    }
}
