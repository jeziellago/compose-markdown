package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import org.junit.Test
import org.junit.Assert.*

class SyntaxHighlightTest {

    @Test
    fun `test SyntaxHighlight creation`() {
        val textLiteral = "highlighted text"
        val syntaxHighlight = SyntaxHighlight(textLiteral)

        assertEquals(textLiteral, syntaxHighlight.textLiteral)
    }

    @Test
    fun `test getOpeningDelimiter returns correct delimiter`() {
        val syntaxHighlight = SyntaxHighlight("test")
        assertEquals("==", syntaxHighlight.openingDelimiter)
    }

    @Test
    fun `test getClosingDelimiter returns correct delimiter`() {
        val syntaxHighlight = SyntaxHighlight("test")
        assertEquals("==", syntaxHighlight.closingDelimiter)
    }

    @Test
    fun `test opening and closing delimiters are the same`() {
        val syntaxHighlight = SyntaxHighlight("test")
        assertEquals(syntaxHighlight.openingDelimiter, syntaxHighlight.closingDelimiter)
    }

    @Test
    fun `test SyntaxHighlight with empty text`() {
        val syntaxHighlight = SyntaxHighlight("")
        assertEquals("", syntaxHighlight.textLiteral)
        assertEquals("==", syntaxHighlight.openingDelimiter)
        assertEquals("==", syntaxHighlight.closingDelimiter)
    }

    @Test
    fun `test SyntaxHighlight with special characters`() {
        val specialText = "test with <>&\"' characters"
        val syntaxHighlight = SyntaxHighlight(specialText)
        assertEquals(specialText, syntaxHighlight.textLiteral)
    }

    @Test
    fun `test SyntaxHighlight with multiline text`() {
        val multilineText = "line 1\nline 2\nline 3"
        val syntaxHighlight = SyntaxHighlight(multilineText)
        assertEquals(multilineText, syntaxHighlight.textLiteral)
    }
}
