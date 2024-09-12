package dev.jeziellago.compose.markdowntext

import androidx.compose.ui.graphics.Color

data class LinkStyle(
    val disableUnderline: Boolean = false,
    val color: Color = Color.Unspecified
) {
    companion object {
        val Default = LinkStyle()
    }
}
