package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.util.Linkify
import android.view.MotionEvent
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.test.core.app.ApplicationProvider
import io.noties.markwon.ext.tables.TableRowSpan
import io.noties.markwon.ext.tables.TableSpan
import io.noties.markwon.ext.tables.TableTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class CustomTextViewTest {

    companion object {
        private const val TEST_MARKDOWN = "# Heading\n\n**Bold** and *italic* text"
        private const val LAYOUT_WIDTH = 300
        private const val LAYOUT_HEIGHT = 200
    }

    private lateinit var context: Context
    private lateinit var textView: CustomTextView

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        textView = CustomTextView(context)
    }

    @Test
    fun `test resetTextState clears text`() {
        textView.text = TEST_MARKDOWN
        assert(textView.text.isNotEmpty()) { "Text should be set" }

        textView.resetTextState()

        assert(textView.text.isEmpty()) { "Text should be cleared after reset" }
    }

    @Test
    fun `test resetTextState removes spans`() {
        val spanned = SpannableString(TEST_MARKDOWN)
        spanned.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            5,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        textView.text = spanned

        assert(textView.text.isNotEmpty()) { "Text should be set initially" }

        textView.resetTextState()

        assert(textView.text.isEmpty()) { "Text should be cleared after reset" }
    }

    @Test
    fun `test removeAllSpans removes all text spans`() {
        val spanned = SpannableString(TEST_MARKDOWN)

        // Add multiple spans
        spanned.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            5,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        spanned.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
            6,
            10,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        textView.text = spanned
        textView.removeAllSpans()

        assert(textView.text.isNotEmpty()) { "Text should still exist" }

        // Text should still be Spannable but without spans
        if (textView.text is Spannable) {
            val spans =
                (textView.text as Spannable).getSpans(0, textView.text.length, Any::class.java)
            assert(spans.isEmpty()) { "All spans should be removed" }
        }
    }

    @Test
    fun `test view survives layout after recycle simulation`() {
        textView.text = TEST_MARKDOWN
        textView.setTextIsSelectable(false)

        assert(textView.text.isNotEmpty()) { "Text should be set" }

        // Simulate view detach (like in LazyColumn recycling)
        textView.onDetachedFromWindow()

        // After detach, should be able to reuse
        assert(textView.text.isEmpty()) { "Text should be cleared after detach" }

        // Now reuse the view with new content
        textView.text = "New content"
        assert(textView.text.toString() == "New content") { "Should accept new text after recycle" }
    }

    @Test
    fun `test text selectable flag is preserved`() {
        textView.text = TEST_MARKDOWN
        textView.setTextIsSelectable(true)

        assert(textView.isTextSelectable) { "Text selectable should be true" }

        textView.setTextIsSelectable(false)

        assert(!textView.isTextSelectable) { "Text selectable should be false" }
    }

    @Test
    fun `test span preservation during text measurement`() {
        val spanned = SpannableString(TEST_MARKDOWN)
        spanned.setSpan(
            android.text.style.ForegroundColorSpan(android.graphics.Color.RED),
            0,
            7,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        textView.text = spanned

        // Measure should not remove spans
        textView.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(
                LAYOUT_WIDTH,
                android.view.View.MeasureSpec.EXACTLY
            ),
            android.view.View.MeasureSpec.makeMeasureSpec(
                LAYOUT_HEIGHT,
                android.view.View.MeasureSpec.EXACTLY
            )
        )

        if (textView.text is Spannable) {
            val spans =
                (textView.text as Spannable).getSpans(0, textView.text.length, Any::class.java)
            assert(spans.isNotEmpty()) { "Spans should be preserved during measurement" }
        }
    }

    @Test
    fun `test selectable tap triggers block click callback`() {
        var clicked = false

        textView.text = "Selectable text"
        textView.setTextIsSelectable(true)
        textView.setOnBlockClickListener { clicked = true }
        measureAndLayoutTextView()

        textView.onTouchEvent(createMotionEvent(MotionEvent.ACTION_DOWN))
        textView.onTouchEvent(createMotionEvent(MotionEvent.ACTION_UP))

        assert(clicked) { "Selectable tap should trigger block click callback" }
    }

    @Test
    fun `test non selectable tap does not trigger internal block click callback`() {
        var clicked = false

        textView.text = "Regular text"
        textView.setTextIsSelectable(false)
        textView.setOnBlockClickListener { clicked = true }
        measureAndLayoutTextView()

        val downHandled = textView.onTouchEvent(createMotionEvent(MotionEvent.ACTION_DOWN))
        val upHandled = textView.onTouchEvent(createMotionEvent(MotionEvent.ACTION_UP))

        assert(!downHandled) { "Non-selectable down event should remain unhandled for Compose clickable" }
        assert(!upHandled) { "Non-selectable up event should remain unhandled for Compose clickable" }
        assert(!clicked) { "Non-selectable tap should not trigger the internal block click callback" }
    }

    @Test
    fun `test disabled link clicks fall through to selectable block click callback`() {
        var blockClicked = false
        var linkClicked = false
        val spanned = SpannableString("Link text")
        spanned.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    linkClicked = true
                }
            },
            0,
            4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spanned
        textView.setTextIsSelectable(true)
        textView.setOnBlockClickListener { blockClicked = true }
        textView.setLinkClicksEnabled(false)
        measureAndLayoutTextView()

        textView.onTouchEvent(createMotionEvent(MotionEvent.ACTION_DOWN))
        textView.onTouchEvent(createMotionEvent(MotionEvent.ACTION_UP))

        assert(!linkClicked) { "Disabled link clicks should not trigger clickable spans" }
        assert(blockClicked) { "Disabled link clicks should fall through to the block click callback" }
    }

    @Test
    fun `test block accessibility builds individual table cells`() {
        val tableText = SpannableString("\u00a0\n\u00a0")
        tableText.setSpan(createTableRowSpan("Country", "Capital"), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tableText.setSpan(createTableRowSpan("Argentina", "Buenos Aires"), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tableText.setSpan(TableSpan(), 0, tableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = tableText
        textView.setBlockLevelAccessibilityEnabled(true)
        measureAndLayoutTextView()
        textView.draw(Canvas(Bitmap.createBitmap(LAYOUT_WIDTH, LAYOUT_HEIGHT, Bitmap.Config.ARGB_8888)))

        val blocks = accessibilityBlocks()
        val textField = checkNotNull(blocks.firstOrNull()).javaClass.getDeclaredField("text")
        textField.isAccessible = true

        assertEquals(
            listOf("Country", "Capital", "Argentina", "Buenos Aires"),
            blocks.map { textField.get(it) },
        )
    }

    @Test
    fun `test table accessibility activates explicit and bare URL links`() {
        val clickedLinks = mutableListOf<String>()
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = false,
            syntaxHighlightColor = Color.LightGray,
            syntaxHighlightTextColor = Color.Unspecified,
            headingBreakColor = Color.Transparent,
            enableUnderlineForLink = true,
            onLinkClicked = clickedLinks::add,
            style = TextStyle(),
        )
        markwon.setMarkdown(
            textView,
            """
                | Type | Link |
                |---|---|
                | Bare | https://example.com/bare |
                | Explicit | [Docs](https://example.com/docs) |
            """.trimIndent(),
        )
        textView.setBlockLevelAccessibilityEnabled(true)
        measureAndDrawTextView()
        measureAndDrawTextView()

        val blocks = accessibilityBlocks()
        val blockClass = checkNotNull(blocks.firstOrNull()).javaClass
        val textField = blockClass.getDeclaredField("text").apply { isAccessible = true }
        val clickableSpanField = blockClass.getDeclaredField("clickableSpan").apply { isAccessible = true }
        val idField = blockClass.getDeclaredField("id").apply { isAccessible = true }
        val linkBlocks = blocks.filter { clickableSpanField.get(it) != null }
        val linksByText = linkBlocks.associateBy { checkNotNull(textField.get(it)).toString() }

        assertEquals(setOf("https://example.com/bare", "Docs"), linksByText.keys)

        performAccessibilityClick(idField.getInt(linksByText.getValue("https://example.com/bare")))
        performAccessibilityClick(idField.getInt(linksByText.getValue("Docs")))

        assertEquals(
            listOf("https://example.com/bare", "https://example.com/docs"),
            clickedLinks,
        )
    }

    @Test
    fun `test table accessibility updates when link clicks are toggled`() {
        val markwon = MarkdownRender.create(
            context = context,
            imageLoader = null,
            linkifyMask = Linkify.WEB_URLS,
            enableSoftBreakAddsNewLine = false,
            syntaxHighlightColor = Color.LightGray,
            syntaxHighlightTextColor = Color.Unspecified,
            headingBreakColor = Color.Transparent,
            enableUnderlineForLink = true,
            onLinkClicked = {},
            style = TextStyle(),
        )
        markwon.setMarkdown(
            textView,
            """
                | Type | Link |
                |---|---|
                | Documentation | [Docs](https://example.com/docs) |
            """.trimIndent(),
        )
        textView.setBlockLevelAccessibilityEnabled(true)
        measureAndDrawTextView()
        measureAndDrawTextView()

        assertEquals(1, clickableAccessibilityBlockCount())

        textView.setLinkClicksEnabled(false)
        assertEquals(0, clickableAccessibilityBlockCount())

        textView.setLinkClicksEnabled(true)
        assertEquals(1, clickableAccessibilityBlockCount())
    }

    @Test
    fun `test stale accessibility block remains a valid hidden node`() {
        textView.text = "Paragraph"
        textView.setBlockLevelAccessibilityEnabled(true)
        measureAndLayoutTextView()

        val helperField = CustomTextView::class.java.getDeclaredField("blockAccessibilityHelper")
        helperField.isAccessible = true
        val helper = checkNotNull(helperField.get(textView))
        val populateNode = helper.javaClass.getDeclaredMethod(
            "onPopulateNodeForVirtualView",
            Int::class.javaPrimitiveType,
            AccessibilityNodeInfoCompat::class.java,
        )
        populateNode.isAccessible = true
        val node = AccessibilityNodeInfoCompat.obtain()

        populateNode.invoke(helper, 999, node)

        assertEquals("", node.contentDescription)
    }

    private fun createTableRowSpan(vararg cells: CharSequence): TableRowSpan {
        return TableRowSpan(
            TableTheme.create(context),
            cells.map { TableRowSpan.Cell(TableRowSpan.ALIGN_LEFT, it) },
            false,
            false,
        )
    }

    private fun accessibilityBlocks(): List<*> {
        val buildBlocks = CustomTextView::class.java.getDeclaredMethod("buildAccessibilityBlocks")
        buildBlocks.isAccessible = true
        return buildBlocks.invoke(textView) as List<*>
    }

    private fun clickableAccessibilityBlockCount(): Int {
        val blocks = accessibilityBlocks()
        val clickableSpanField = checkNotNull(blocks.firstOrNull())
            .javaClass
            .getDeclaredField("clickableSpan")
            .apply { isAccessible = true }
        return blocks.count { clickableSpanField.get(it) != null }
    }

    private fun performAccessibilityClick(virtualViewId: Int) {
        val helperField = CustomTextView::class.java.getDeclaredField("blockAccessibilityHelper")
        helperField.isAccessible = true
        val helper = checkNotNull(helperField.get(textView))
        val performAction = helper.javaClass.getDeclaredMethod(
            "onPerformActionForVirtualView",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType,
            android.os.Bundle::class.java,
        )
        performAction.isAccessible = true

        assertEquals(
            true,
            performAction.invoke(helper, virtualViewId, AccessibilityNodeInfoCompat.ACTION_CLICK, null),
        )
    }

    private fun measureAndDrawTextView() {
        measureAndLayoutTextView()
        textView.draw(Canvas(Bitmap.createBitmap(LAYOUT_WIDTH, LAYOUT_HEIGHT, Bitmap.Config.ARGB_8888)))
    }

    private fun measureAndLayoutTextView() {
        textView.measure(
            View.MeasureSpec.makeMeasureSpec(LAYOUT_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(LAYOUT_HEIGHT, View.MeasureSpec.EXACTLY)
        )
        textView.layout(0, 0, LAYOUT_WIDTH, LAYOUT_HEIGHT)
    }

    private fun createMotionEvent(action: Int): MotionEvent {
        return MotionEvent.obtain(
            0L,
            0L,
            action,
            10f,
            10f,
            0
        )
    }
}
