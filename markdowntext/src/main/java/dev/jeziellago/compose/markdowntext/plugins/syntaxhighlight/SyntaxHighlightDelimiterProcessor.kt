package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import org.commonmark.node.Node
import org.commonmark.node.Text
import org.commonmark.parser.delimiter.DelimiterProcessor
import org.commonmark.parser.delimiter.DelimiterRun

class SyntaxHighlightDelimiterProcessor : DelimiterProcessor {
    override fun getOpeningCharacter(): Char = '='

    override fun getClosingCharacter(): Char = '='

    override fun getMinLength(): Int = 2

    override fun getDelimiterUse(opener: DelimiterRun, closer: DelimiterRun): Int {
        return if (opener.length() >= 2 && closer.length() >= 2) {
            // Use exactly two delimiters even if we have more, and don't care about internal openers/closers.
            2
        } else {
            0
        }
    }

    override fun process(opener: Text, closer: Text, delimiterCount: Int) {
        // Wrap nodes between delimiters in SyntaxHighlight.
        val syntaxHighlight: Node = SyntaxHighlight()

        var tmp = opener.next
        while (tmp != null && tmp !== closer) {
            val next = tmp.next
            syntaxHighlight.appendChild(tmp)
            tmp = next
        }

        opener.insertAfter(syntaxHighlight)
    }
}
