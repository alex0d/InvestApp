package ru.alex0d.investapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTheme
import io.github.alexzhirkevich.cupertino.adaptive.CupertinoThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.MaterialThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.Theme

private val materialDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val materialLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

expect fun determineTheme(): Theme

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun InvestAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    theme: Theme = determineTheme(),
    content: @Composable () -> Unit
) {
    AdaptiveTheme(
        target = theme,
        material = MaterialThemeSpec.Default(
            colorScheme = if (darkTheme) materialDarkColorScheme else materialLightColorScheme,
        ),
        cupertino = CupertinoThemeSpec.Default(),
        content = content
    )
}