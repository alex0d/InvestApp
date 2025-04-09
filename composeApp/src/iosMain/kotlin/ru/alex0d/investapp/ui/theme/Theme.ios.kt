package ru.alex0d.investapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.MaterialThemeSpec
import io.github.alexzhirkevich.cupertino.adaptive.Theme

actual fun determineTheme() = Theme.Cupertino

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
actual fun materialThemeSpec(
    darkTheme: Boolean,
    darkColorScheme: ColorScheme,
    lightColorScheme: ColorScheme,
): MaterialThemeSpec = MaterialThemeSpec.Default()

@ReadOnlyComposable
@Composable
actual fun isSystemInDarkAdaptiveTheme() = false