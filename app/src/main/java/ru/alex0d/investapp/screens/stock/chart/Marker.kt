package ru.alex0d.investapp.screens.stock.chart

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.LayeredComponent
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape

@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = CorneredShape.rounded(allPercent = 100)
    val labelBackground = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.surface),
        shape = labelBackgroundShape
    )
    val label = rememberTextComponent(
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily(Typeface.MONOSPACE),
            textAlign = TextAlign.Center,
        ),
        background = labelBackground,
        padding = Insets(horizontal = 8.dp, vertical = 4.dp),
        minWidth = TextComponent.MinWidth.fixed(40.dp),
    )
    val indicatorFrontComponent = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.surface),
        shape = CorneredShape.Pill,
    )
    val indicatorCenterComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicator = LayeredComponent(
        back = indicatorRearComponent,
        front = LayeredComponent(
            back = indicatorCenterComponent,
            front = indicatorFrontComponent,
            padding = Insets(all = 5.dp),
        ),
        padding = Insets(all = 10.dp),
    )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        DefaultCartesianMarker(
            label = label,
            labelPosition = labelPosition,
            indicator = if (showIndicator) { _ -> indicator } else null,
            indicatorSize = 36.dp,
            guideline = guideline,
        )
    }
}
