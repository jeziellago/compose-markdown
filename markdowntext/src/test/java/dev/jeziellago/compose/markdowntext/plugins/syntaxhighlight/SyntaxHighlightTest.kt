package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import org.junit.Test
import org.junit.Assert.*

class SyntaxHighlightTest {

    @Test
    fun `syntax highlight node keeps the literal text and delimiter pair`() {
        val textLiteral = "highlighted text"
        val syntaxHighlight = SyntaxHighlight(textLiteral)

        assertEquals(textLiteral, syntaxHighlight.textLiteral)
        assertEquals("==", syntaxHighlight.openingDelimiter)
        assertEquals("==", syntaxHighlight.closingDelimiter)
    }
}
