package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import io.mockk.*
import org.commonmark.Extension
import org.commonmark.parser.Parser
import org.commonmark.renderer.text.TextContentRenderer
import org.junit.Test

class SyntaxHighlightExtensionTest {

    @Test
    fun `test create returns Extension instance`() {
        val extension = SyntaxHighlightExtension.create()
        assert(extension is Extension)
        assert(extension is SyntaxHighlightExtension)
    }

    @Test
    fun `test extend parser builder adds delimiter processor`() {
        val extension = SyntaxHighlightExtension.create() as SyntaxHighlightExtension
        val mockParserBuilder = mockk<Parser.Builder>(relaxed = true)

        every { mockParserBuilder.customDelimiterProcessor(any()) } returns mockParserBuilder

        extension.extend(mockParserBuilder)

        verify { 
            mockParserBuilder.customDelimiterProcessor(any<SyntaxHighlightDelimiterProcessor>())
        }
    }

    @Test
    fun `test extend renderer builder adds node renderer factory`() {
        val extension = SyntaxHighlightExtension.create() as SyntaxHighlightExtension
        val mockRendererBuilder = mockk<TextContentRenderer.Builder>(relaxed = true)

        every { mockRendererBuilder.nodeRendererFactory(any()) } returns mockRendererBuilder

        extension.extend(mockRendererBuilder)

        verify { mockRendererBuilder.nodeRendererFactory(any()) }
    }

    @Test
    fun `test delimiter processor configuration`() {
        val extension = SyntaxHighlightExtension.create() as SyntaxHighlightExtension
        val mockParserBuilder = mockk<Parser.Builder>(relaxed = true)
        
        var capturedProcessor: SyntaxHighlightDelimiterProcessor? = null
        every { 
            mockParserBuilder.customDelimiterProcessor(capture(slot<SyntaxHighlightDelimiterProcessor>()))
        } answers {
            capturedProcessor = firstArg()
            mockParserBuilder
        }

        extension.extend(mockParserBuilder)

        assert(capturedProcessor != null)
        assert(capturedProcessor!!.openingCharacter == '=')
        assert(capturedProcessor!!.closingCharacter == '=')
        assert(capturedProcessor!!.minLength == 2)
    }

    @Test
    fun `test node renderer factory creates SyntaxHighlightNodeRenderer`() {
        val extension = SyntaxHighlightExtension.create() as SyntaxHighlightExtension
        val mockRendererBuilder = mockk<TextContentRenderer.Builder>(relaxed = true)
        val mockContext = mockk<org.commonmark.renderer.text.TextContentNodeRendererContext>(relaxed = true)
        val mockWriter = mockk<org.commonmark.renderer.text.TextContentWriter>(relaxed = true)
        
        every { mockContext.writer } returns mockWriter
        
        extension.extend(mockRendererBuilder)

        verify { mockRendererBuilder.nodeRendererFactory(any()) }
    }

    @Test
    fun `test extension is ParserExtension`() {
        val extension = SyntaxHighlightExtension.create()
        assert(extension is org.commonmark.parser.Parser.ParserExtension)
    }

    @Test
    fun `test extension is TextContentRendererExtension`() {
        val extension = SyntaxHighlightExtension.create()
        assert(extension is org.commonmark.renderer.text.TextContentRenderer.TextContentRendererExtension)
    }

    @Test
    fun `test create returns same type of extension`() {
        val extension1 = SyntaxHighlightExtension.create()
        val extension2 = SyntaxHighlightExtension.create()

        assert(extension1.javaClass == extension2.javaClass)
        assert(extension1 !== extension2)
    }
}
