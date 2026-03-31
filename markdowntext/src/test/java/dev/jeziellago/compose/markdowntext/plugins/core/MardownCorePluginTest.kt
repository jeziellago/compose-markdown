package dev.jeziellago.compose.markdowntext.plugins.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.mockk.*
import io.noties.markwon.core.MarkwonTheme
import org.junit.Before
import org.junit.Test

class MardownCorePluginTest {

    private lateinit var plugin: MardownCorePlugin
    private lateinit var mockThemeBuilder: MarkwonTheme.Builder

    @Before
    fun setUp() {
        plugin = MardownCorePlugin(
            syntaxHighlightColor = Color.Gray.toArgb(),
            syntaxHighlightTextColor = Color.Black.toArgb(),
            enableUnderlineForLink = true
        )

        mockThemeBuilder = mockk<MarkwonTheme.Builder>(relaxed = true)
    }

    @Test
    fun `configureTheme applies code colors and link underline`() {
        every { mockThemeBuilder.codeBackgroundColor(any()) } returns mockThemeBuilder
        every { mockThemeBuilder.isLinkUnderlined(any()) } returns mockThemeBuilder
        every { mockThemeBuilder.codeTextColor(any()) } returns mockThemeBuilder

        plugin.configureTheme(mockThemeBuilder)

        verify { mockThemeBuilder.codeBackgroundColor(Color.Gray.toArgb()) }
        verify { mockThemeBuilder.isLinkUnderlined(true) }
        verify { mockThemeBuilder.codeTextColor(Color.Black.toArgb()) }
    }

    @Test
    fun `configureTheme skips code text color when it is unspecified`() {
        val pluginWithUnspecifiedColor = MardownCorePlugin(
            syntaxHighlightColor = Color.Gray.toArgb(),
            syntaxHighlightTextColor = Color.Unspecified.toArgb(),
            enableUnderlineForLink = true
        )
        
        every { mockThemeBuilder.codeBackgroundColor(any()) } returns mockThemeBuilder
        every { mockThemeBuilder.isLinkUnderlined(any()) } returns mockThemeBuilder

        pluginWithUnspecifiedColor.configureTheme(mockThemeBuilder)

        verify { mockThemeBuilder.codeBackgroundColor(Color.Gray.toArgb()) }
        verify { mockThemeBuilder.isLinkUnderlined(true) }
        verify(exactly = 0) { mockThemeBuilder.codeTextColor(any()) }
    }
}
