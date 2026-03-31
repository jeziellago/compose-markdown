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
    fun `delimiter use returns min length when both runs are long enough`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 2
        every { mockCloser.length() } returns 2

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 2)
    }

    @Test
    fun `delimiter use returns zero when opener is too short`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 1
        every { mockCloser.length() } returns 2

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 0)
    }

    @Test
    fun `delimiter use is capped at min length`() {
        val mockOpener = mockk<DelimiterRun>()
        val mockCloser = mockk<DelimiterRun>()

        every { mockOpener.length() } returns 4
        every { mockCloser.length() } returns 3

        val result = processor.getDelimiterUse(mockOpener, mockCloser)

        assert(result == 2) // Should still return minLength
    }

    @Test
    fun `process inserts a syntax highlight node after the opener`() {
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
    fun `process keeps the opener literal on the inserted node`() {
        val mockOpener = mockk<Text>(relaxed = true)
        val mockCloser = mockk<Text>(relaxed = true)
        val openerLiteral = "highlight me"
        val capturedNode = slot<SyntaxHighlight>()

        every { mockOpener.literal } returns openerLiteral
        every { mockOpener.next } returns mockCloser
        every { mockOpener.insertAfter(capture(capturedNode)) } just Runs

        processor.process(mockOpener, mockCloser, 2)

        assert(capturedNode.isCaptured)
        assert(capturedNode.captured.textLiteral == openerLiteral)
    }
}
