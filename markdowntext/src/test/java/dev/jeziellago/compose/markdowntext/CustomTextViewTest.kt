package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import androidx.test.core.app.ApplicationProvider
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
