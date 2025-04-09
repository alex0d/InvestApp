package ru.alex0d.investapp.utils.extensions

import androidx.compose.ui.graphics.Color

fun Color.Companion.fromHex(colorString: String): Color {
    return Color(colorString.removePrefix("#").toLong(16) or 0x00000000FF000000)
}