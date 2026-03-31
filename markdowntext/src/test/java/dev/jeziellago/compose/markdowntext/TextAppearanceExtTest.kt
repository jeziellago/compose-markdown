package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TextAppearanceExtTest {

    companion object {
        private const val LAYOUT_WIDTH = 100
        private const val LAYOUT_HEIGHT = 100
        private const val LONG_TEXT =
            "This is a very long text that will definitely overflow when constrained to a single line and should be truncated with an ellipsis character at the end"
    }

    private lateinit var context: Context
    private lateinit var textView: TextView

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        textView = TextView(context)
    }

    @Test
    fun `enableTextOverflow handles long text without crashing`() {
        textView.text = LONG_TEXT
        textView.maxLines = 1

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
        textView.layout(0, 0, LAYOUT_WIDTH, LAYOUT_HEIGHT)

        try {
            textView.enableTextOverflow()
            assert(true)
        } catch (e: StringIndexOutOfBoundsException) {
            assert(false) { "enableTextOverflow should not throw StringIndexOutOfBoundsException: ${e.message}" }
        }
    }

    @Test
    fun `enableTextOverflow leaves text unchanged when maxLines is unlimited`() {
        textView.text = LONG_TEXT
        textView.maxLines = -1 // Infinity

        val originalText = textView.text.toString()

        try {
            textView.enableTextOverflow()
            assert(textView.text.toString() == originalText)
        } catch (e: Exception) {
            assert(false) { "enableTextOverflow should handle maxLines=-1: ${e.message}" }
        }
    }

    @Test
    fun `enableTextOverflow keeps spanned text as spanned`() {
        val spanned = android.text.SpannableString(LONG_TEXT)
        textView.text = spanned
        textView.maxLines = 1

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
        textView.layout(0, 0, LAYOUT_WIDTH, LAYOUT_HEIGHT)

        try {
            textView.enableTextOverflow()
            assert(textView.text is android.text.Spanned)
        } catch (e: Exception) {
            assert(false) { "Should preserve text span type: ${e.message}" }
        }
    }
}
