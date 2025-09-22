package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.text.Spanned
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.test.core.app.ApplicationProvider
import coil.ImageLoader
import io.mockk.*
import io.noties.markwon.Markwon
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MarkdownRenderTest {

    companion object {
        private const val DEFAULT_FONT_SIZE = 16
        private const val SMALL_FONT_SIZE = 12
        private const val MEDIUM_FONT_SIZE = 14
        private const val LARGE_FONT_SIZE = 18
        private const val EXTRA_LARGE_FONT_SIZE = 24
    }

    private lateinit var context: Context
    private lateinit var mockImageLoader: ImageLoader
    private lateinit var mockTextView: TextView
    private lateinit var mockSpanned: Spanned

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockImageLoader = mockk<ImageLoader>()
        mockTextView = mockk<TextView>(relaxed = true)
        mockSpanned = mockk<Spanned>()
    }

    @Test
    fun `test create with default parameters`() {
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = true,
            syntaxHighlightColor = Color.Gray,
            syntaxHighlightTextColor = Color.Black,
            headingBreakColor = Color.Transparent,
            enableUnderlineForLink = true,
            style = TextStyle(fontSize = DEFAULT_FONT_SIZE.sp)
        )

        assert(markwon is Markwon)
    }

    @Test
    fun `test create with custom ImageLoader`() {
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = mockImageLoader,
            linkifyMask = Linkify.EMAIL_ADDRESSES,
            enableSoftBreakAddsNewLine = false,
            syntaxHighlightColor = Color.Blue,
            syntaxHighlightTextColor = Color.White,
            headingBreakColor = Color.Red,
            enableUnderlineForLink = false,
            style = TextStyle(fontSize = MEDIUM_FONT_SIZE.sp)
        )

        assert(markwon is Markwon)
    }

    @Test
    fun `test create with callbacks`() {
        val beforeCallback = mockk<(TextView, Spanned) -> Unit>()
        every { beforeCallback.invoke(any(), any()) } just Runs
        
        val afterCallback = mockk<(TextView) -> Unit>()
        every { afterCallback.invoke(any()) } just Runs
        
        val linkClickCallback = mockk<(String) -> Unit>()
        every { linkClickCallback.invoke(any()) } just Runs

        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = true,
            syntaxHighlightColor = Color.Gray,
            syntaxHighlightTextColor = Color.Black,
            headingBreakColor = Color.Transparent,
            enableUnderlineForLink = true,
            beforeSetMarkdown = beforeCallback,
            afterSetMarkdown = afterCallback,
            onLinkClicked = linkClickCallback,
            style = TextStyle(fontSize = DEFAULT_FONT_SIZE.sp)
        )

        assert(markwon is Markwon)
    }

    @Test
    fun `test create with different linkify masks`() {
        val linkifyMasks = listOf(
            Linkify.WEB_URLS,
            Linkify.EMAIL_ADDRESSES,
            Linkify.PHONE_NUMBERS,
            Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES
        )

        linkifyMasks.forEach { mask ->
            val markwon = MarkdownRender.create(
                context = context,
                imageLoader = null,
                linkifyMask = mask,
                enableSoftBreakAddsNewLine = true,
                syntaxHighlightColor = Color.Gray,
                syntaxHighlightTextColor = Color.Black,
                headingBreakColor = Color.Transparent,
                enableUnderlineForLink = true,
                style = TextStyle(fontSize = DEFAULT_FONT_SIZE.sp)
            )

            assert(markwon is Markwon)
        }
    }

    @Test
    fun `test create with enableSoftBreakAddsNewLine false`() {
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = false,
            syntaxHighlightColor = Color.Gray,
            syntaxHighlightTextColor = Color.Black,
            headingBreakColor = Color.Transparent,
            enableUnderlineForLink = true,
            style = TextStyle(fontSize = DEFAULT_FONT_SIZE.sp)
        )

        assert(markwon is Markwon)
    }

    @Test
    fun `test create with different colors`() {
        val colors = listOf(
            Triple(Color.Red, Color.Blue, Color.Green),
            Triple(Color.Black, Color.White, Color.Yellow),
            Triple(Color.Transparent, Color.Unspecified, Color.Cyan)
        )

        colors.forEach { (syntaxColor, textColor, breakColor) ->
            val markwon = MarkdownRender.create(
                context = context,
                imageLoader = null,
                linkifyMask = Linkify.WEB_URLS,
                enableSoftBreakAddsNewLine = true,
                syntaxHighlightColor = syntaxColor,
                syntaxHighlightTextColor = textColor,
                headingBreakColor = breakColor,
                enableUnderlineForLink = true,
                style = TextStyle(fontSize = DEFAULT_FONT_SIZE.sp)
            )

            assert(markwon is Markwon)
        }
    }

    @Test
    fun `test create with different text styles`() {
        val textStyles = listOf(
            TextStyle(fontSize = SMALL_FONT_SIZE.sp),
            TextStyle(fontSize = LARGE_FONT_SIZE.sp),
            TextStyle(fontSize = EXTRA_LARGE_FONT_SIZE.sp)
        )

        textStyles.forEach { style ->
            val markwon = MarkdownRender.create(
                context = context,
                imageLoader = null,
                linkifyMask = Linkify.WEB_URLS,
                enableSoftBreakAddsNewLine = true,
                syntaxHighlightColor = Color.Gray,
                syntaxHighlightTextColor = Color.Black,
                headingBreakColor = Color.Transparent,
                enableUnderlineForLink = true,
                style = style
            )

            assert(markwon is Markwon)
        }
    }

    @Test
    fun `test create handles null callbacks gracefully`() {
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = true,
            syntaxHighlightColor = Color.Gray,
            syntaxHighlightTextColor = Color.Black,
            headingBreakColor = Color.Transparent,
            enableUnderlineForLink = true,
            beforeSetMarkdown = null,
            afterSetMarkdown = null,
            onLinkClicked = null,
            style = TextStyle(fontSize = DEFAULT_FONT_SIZE.sp)
        )

        assert(markwon is Markwon)
    }
}
