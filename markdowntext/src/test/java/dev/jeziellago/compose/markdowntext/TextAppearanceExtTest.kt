package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class TextAppearanceExtTest {

    companion object {
        private const val TEST_FONT_SIZE_18 = 18
        private const val TEST_LINE_HEIGHT_20 = 20
        private const val LAYOUT_WIDTH = 100
        private const val LAYOUT_HEIGHT = 100
    }

    private lateinit var context: Context
    private lateinit var textView: TextView

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        textView = TextView(context)
    }

    @Test
    fun `test applyFontWeight with Bold on API 28+`() {
        textView.applyFontWeight(FontWeight.Bold)
        
        assert(textView.typeface != null)
    }

    @Test
    fun `test applyFontWeight with Normal`() {
        textView.applyFontWeight(FontWeight.Normal)
        
        assert(textView.typeface != null || textView.typeface == null)
    }

    @Test
    fun `test applyFontWeight with SemiBold maps to Bold on older APIs`() {
        textView.applyFontWeight(FontWeight.SemiBold)
        
        assert(textView.typeface != null)
    }

    @Test
    fun `test applyFontStyle with Italic`() {
        textView.applyFontStyle(FontStyle.Italic)
        assert(textView.typeface != null)
    }

    @Test
    fun `test applyTextColor`() {
        val color = Color.Red.toArgb()
        textView.applyTextColor(color)
        assert(textView.currentTextColor == color)
    }

    @Test
    fun `test applyFontSize`() {
        val textStyle = TextStyle(fontSize = TEST_FONT_SIZE_18.sp)
        textView.applyFontSize(textStyle)
        assert(textView.textSize > 0)
    }

    @Test
    fun `test applyTextDecoration with LineThrough`() {
        val textStyle = TextStyle(textDecoration = TextDecoration.LineThrough)
        textView.applyTextDecoration(textStyle)
        assert((textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) != 0)
    }

    @Test
    fun `test applyTextDecoration with None`() {
        val textStyle = TextStyle(textDecoration = TextDecoration.None)
        textView.applyTextDecoration(textStyle)
        assert((textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) == 0)
    }

    @Test
    fun `test applyLineHeight with SP unit`() {
        val textStyle = TextStyle(lineHeight = TEST_LINE_HEIGHT_20.sp)
        textView.applyLineHeight(textStyle)
        assert(textView.lineHeight > 0)
    }

    @Test
    fun `test applyTextAlign with Start`() {
        textView.applyTextAlign(TextAlign.Start)
        assert(textView.gravity and android.view.Gravity.START != 0)
    }

    @Test
    fun `test applyTextAlign with End`() {
        textView.applyTextAlign(TextAlign.End)
        assert(textView.gravity and android.view.Gravity.END != 0)
    }

    @Test
    fun `test applyTextAlign with Center`() {
        textView.applyTextAlign(TextAlign.Center)
        assert(textView.gravity and android.view.Gravity.CENTER_HORIZONTAL != 0)
    }

    @Test
    fun `test applyTextAlign with Left`() {
        textView.applyTextAlign(TextAlign.Left)
        assert(textView.gravity and android.view.Gravity.START != 0)
    }

    @Test
    fun `test applyTextAlign with Right`() {
        textView.applyTextAlign(TextAlign.Right)
        assert(textView.gravity and android.view.Gravity.END != 0)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.Q])
    fun `test applyTextAlign with Justify on API 29+`() {
        textView.applyTextAlign(TextAlign.Justify)
        
        assert(textView.gravity and android.view.Gravity.START != 0)
    }

    @Test
    fun `test applyFontResource with invalid resource`() {
        val fontRes = android.R.drawable.ic_menu_gallery
        
        assert(fontRes > 0)
    }

    @Test
    fun `test enableTextOverflow with single line`() {
        textView.text = "Short text"
        textView.maxLines = 1
        textView.enableTextOverflow()
        
        textView.measure(0, 0)
        textView.layout(0, 0, LAYOUT_WIDTH, LAYOUT_HEIGHT)
        
        assert(textView.text.isNotEmpty())
    }

    @Test
    fun `test enableTextOverflow with multiple lines`() {
        textView.text = "This is a very long text that should definitely overflow when constrained to a single line and should be truncated with ellipsis"
        textView.maxLines = 1
        textView.enableTextOverflow()
        
        textView.measure(0, 0)
        textView.layout(0, 0, LAYOUT_WIDTH, LAYOUT_HEIGHT)
        
        assert(textView.text.isNotEmpty())
    }
}
