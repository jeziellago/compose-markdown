package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MarkdownRenderTest {

    private lateinit var context: Context
    private lateinit var textView: TextView

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        textView = TextView(context)
    }

    @Test
    fun `single newline renders as space when soft breaks are disabled`() {
        renderMarkdown(enableSoftBreakAddsNewLine = false, markdown = "foo\nbar")

        val renderedText = textView.text.toString()
        assert(renderedText == "foo bar") {
            "Expected a space for a soft break, but was '$renderedText'"
        }
    }

    @Test
    fun `single newline renders as line break when soft breaks are enabled`() {
        renderMarkdown(enableSoftBreakAddsNewLine = true, markdown = "foo\nbar")

        val renderedText = textView.text.toString()
        assert(renderedText == "foo\nbar") {
            "Expected a newline for an enabled soft break plugin, but was '$renderedText'"
        }
    }

    @Test
    fun `soft break rendering preserves all text`() {
        val markdown = "Line one\nLine two\nLine three"
        renderMarkdown(enableSoftBreakAddsNewLine = false, markdown = markdown)

        val renderedText = textView.text.toString()
        assert(renderedText.contains("Line one")) { "Should contain 'Line one'" }
        assert(renderedText.contains("Line two")) { "Should contain 'Line two'" }
        assert(renderedText.contains("Line three")) { "Should contain 'Line three'" }
    }

    private fun renderMarkdown(enableSoftBreakAddsNewLine: Boolean, markdown: String) {
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = android.text.util.Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = enableSoftBreakAddsNewLine,
            syntaxHighlightColor = androidx.compose.ui.graphics.Color.LightGray,
            syntaxHighlightTextColor = androidx.compose.ui.graphics.Color.Unspecified,
            headingBreakColor = androidx.compose.ui.graphics.Color.Transparent,
            enableUnderlineForLink = true,
            style = androidx.compose.ui.text.TextStyle()
        )
        markwon.setMarkdown(textView, markdown)
    }
}
