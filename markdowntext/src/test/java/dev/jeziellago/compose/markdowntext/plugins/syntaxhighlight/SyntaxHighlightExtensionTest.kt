package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import io.mockk.*
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.commonmark.renderer.text.TextContentRenderer
import org.junit.Test

class SyntaxHighlightExtensionTest {

    @Test
    fun `extend parser configures == delimiter processor`() {
        val extension = SyntaxHighlightExtension.create() as SyntaxHighlightExtension
        val mockParserBuilder = mockk<Parser.Builder>(relaxed = true)
        val processorSlot = slot<SyntaxHighlightDelimiterProcessor>()
        every { mockParserBuilder.customDelimiterProcessor(capture(processorSlot)) } returns mockParserBuilder

        extension.extend(mockParserBuilder)

        assert(processorSlot.isCaptured)
        assert(processorSlot.captured.openingCharacter == '=')
        assert(processorSlot.captured.closingCharacter == '=')
        assert(processorSlot.captured.minLength == 2)
    }

    @Test
    fun `extend renderer registers a node renderer factory`() {
        val extension = SyntaxHighlightExtension.create() as SyntaxHighlightExtension
        val mockRendererBuilder = mockk<TextContentRenderer.Builder>(relaxed = true)
        every { mockRendererBuilder.nodeRendererFactory(any()) } returns mockRendererBuilder

        extension.extend(mockRendererBuilder)

        verify { mockRendererBuilder.nodeRendererFactory(any()) }
    }
}
