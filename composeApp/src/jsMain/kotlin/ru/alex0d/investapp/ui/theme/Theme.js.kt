package ru.alex0d.investapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.MaterialThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.Theme

@ReadOnlyComposable
@Composable
actual fun isSystemInDarkAdaptiveTheme() = isSystemInDarkTheme()

actual fun determineTheme() = Theme.Material3

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
actual fun materialThemeSpec(
    darkTheme: Boolean,
    darkColorScheme: ColorScheme,
    lightColorScheme: ColorScheme
): MaterialThemeSpec {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme
    return MaterialThemeSpec.Default(colorScheme = colorScheme)
}