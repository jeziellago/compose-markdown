package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import io.mockk.*
import org.commonmark.node.Text
import org.commonmark.parser.delimiter.DelimiterRun
import org.junit.Before
import org.junit.Test

class SyntaxHighlightDelimiterProcessorTest {

    private lateinit var processor: SyntaxHighlightDelimiterProcessor

    @Before
    fun setUp() {
        processor = SyntaxHighlightDelimiterProcessor(
            openingCharacter = '=',
            closingCharacter = '=',
            minLength = 2
        )
    }

    @Test
    fun `test getOpeningCharacter returns correct character`() {
        assert(processor.openingCharacter == '=')
    }

    @Test
    fun `test getClosingCharacter returns correct character`() {
        assert(processor.closingCharacter == '=')
    }

    @Test
    fun `test getMinLength returns correct length`() {
        assert(processor.minLength == 2)
    }

    @Test
    fun `test getDelimiterUse with sufficient length returns minLength`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 2
        every { mockCloser.length() } returns 2

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 2)
    }

    @Test
    fun `test getDelimiterUse with insufficient opener length returns 0`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 1
        every { mockCloser.length() } returns 2

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 0)
    }

    @Test
    fun `test getDelimiterUse with insufficient closer length returns 0`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 2
        every { mockCloser.length() } returns 1

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 0)
    }

    @Test
    fun `test getDelimiterUse with more than minLength still returns minLength`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 4
        every { mockCloser.length() } returns 3

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 2) // Should still return minLength
    }

    @Test
    fun `test process wraps nodes in SyntaxHighlight`() {
        val mockOpener = mockk<Text>(relaxed = true)
        val mockCloser = mockk<Text>(relaxed = true)
        val mockMiddleNode = mockk<org.commonmark.node.Node>(relaxed = true)

        every { mockOpener.literal } returns "test content"
        every { mockOpener.next } returns mockMiddleNode
        every { mockMiddleNode.next } returns mockCloser
        every { mockOpener.insertAfter(any()) } just Runs

        processor.process(mockOpener, mockCloser, 2)

        verify { mockOpener.insertAfter(any<SyntaxHighlight>()) }
    }

    @Test
    fun `test process with no nodes between delimiters`() {
        val mockOpener = mockk<Text>(relaxed = true)
        val mockCloser = mockk<Text>(relaxed = true)

        every { mockOpener.literal } returns "test"
        every { mockOpener.next } returns mockCloser
        every { mockOpener.insertAfter(any()) } just Runs

        processor.process(mockOpener, mockCloser, 2)

        verify { mockOpener.insertAfter(any<SyntaxHighlight>()) }
    }

    @Test
    fun `test process creates SyntaxHighlight with opener literal`() {
        val mockOpener = mockk<Text>(relaxed = true)
        val mockCloser = mockk<Text>(relaxed = true)
        val openerLiteral = "test content"

        every { mockOpener.literal } returns openerLiteral
        every { mockOpener.next } returns mockCloser
        every { mockOpener.insertAfter(any()) } just Runs

        processor.process(mockOpener, mockCloser, 2)

        verify { mockOpener.insertAfter(any()) }
    }

    @Test
    fun `test processor with custom characters`() {
        val customProcessor = SyntaxHighlightDelimiterProcessor(
            openingCharacter = '*',
            closingCharacter = '*',
            minLength = 1
        )

        assert(customProcessor.openingCharacter == '*')
        assert(customProcessor.closingCharacter == '*')
        assert(customProcessor.minLength == 1)
    }

    @Test
    fun `test processor with different opening and closing characters`() {
        val customProcessor = SyntaxHighlightDelimiterProcessor(
            openingCharacter = '[',
            closingCharacter = ']',
            minLength = 1
        )

        assert(customProcessor.openingCharacter == '[')
        assert(customProcessor.closingCharacter == ']')
        assert(customProcessor.minLength == 1)
    }
}
