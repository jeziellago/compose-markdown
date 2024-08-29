package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import android.text.style.BackgroundColorSpan
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.MarkwonVisitor
import org.commonmark.parser.Parser

class SyntaxHighlightPlugin(
    private val syntaxHighlightColor: Int
) : AbstractMarkwonPlugin() {

    override fun configureParser(builder: Parser.Builder) {
        builder.extensions(setOf(SyntaxHighlightExtension.create()))
    }

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(
            SyntaxHighlight::class.java
        ) { _, _ -> BackgroundColorSpan(syntaxHighlightColor) }
    }

    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
        builder.on(
            SyntaxHighlight::class.java
        ) { visitor, syntaxHighlight ->
            val length = visitor.length()
            visitor.visitChildren(syntaxHighlight)
            visitor.setSpansForNodeOptional(syntaxHighlight, length)
        }
    }
}