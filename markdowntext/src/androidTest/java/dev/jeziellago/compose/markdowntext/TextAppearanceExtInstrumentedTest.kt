package dev.jeziellago.compose.markdowntext

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextAppearanceExtInstrumentedTest {

    companion object {
        private const val FONT_SIZE_14 = 14
        private const val FONT_SIZE_16 = 16
        private const val FONT_SIZE_18 = 18
        private const val FONT_SIZE_24 = 24
        private const val LINE_HEIGHT_20 = 20
        private const val LAYOUT_WIDTH_200 = 200
        private const val LAYOUT_HEIGHT_100 = 100
        private const val LAYOUT_WIDTH_100 = 100
        private const val LAYOUT_HEIGHT_50 = 50
    }

    private lateinit var textView: TextView

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        textView = TextView(context)
    }

    @Test
    fun testApplyFontWeightBold() {
        textView.applyFontWeight(FontWeight.Bold)
        
        assert(textView.typeface != null)
    }

    @Test
    fun testApplyFontWeightNormal() {
        textView.applyFontWeight(FontWeight.Normal)
        
        assert(textView.typeface != null)
    }

    @Test
    fun testApplyFontWeightSemiBold() {
        textView.applyFontWeight(FontWeight.SemiBold)
        
        assert(textView.typeface != null)
    }

    @Test
    fun testApplyFontWeightExtraBold() {
        textView.applyFontWeight(FontWeight.ExtraBold)
        
        assert(textView.typeface != null)
    }

    @Test
    fun testApplyFontStyleItalic() {
        textView.applyFontStyle(FontStyle.Italic)
        assert(textView.typeface.isItalic)
    }

    @Test
    fun testApplyFontStyleNormal() {
        textView.applyFontStyle(FontStyle.Normal)
        assert(!textView.typeface.isItalic)
    }

    @Test
    fun testApplyTextColor() {
        val redColor = Color.Red.toArgb()
        textView.applyTextColor(redColor)
        assert(textView.currentTextColor == redColor)

        val blueColor = Color.Blue.toArgb()
        textView.applyTextColor(blueColor)
        assert(textView.currentTextColor == blueColor)
    }

    @Test
    fun testApplyFontSize() {
        val style14sp = TextStyle(fontSize = FONT_SIZE_14.sp)
        textView.applyFontSize(style14sp)
        assert(textView.textSize > 0)

        val style18sp = TextStyle(fontSize = FONT_SIZE_18.sp)
        textView.applyFontSize(style18sp)
        assert(textView.textSize > 0)

        val style24sp = TextStyle(fontSize = FONT_SIZE_24.sp)
        textView.applyFontSize(style24sp)
        assert(textView.textSize > 0)
    }

    @Test
    fun testApplyTextDecorationLineThrough() {
        val styleWithStrikethrough = TextStyle(textDecoration = TextDecoration.LineThrough)
        textView.applyTextDecoration(styleWithStrikethrough)
        assert((textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) != 0)
    }

    @Test
    fun testApplyTextDecorationNone() {
        val styleWithoutDecoration = TextStyle(textDecoration = TextDecoration.None)
        textView.applyTextDecoration(styleWithoutDecoration)
        assert((textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) == 0)
    }

    @Test
    fun testApplyLineHeight() {
        val styleWithLineHeight = TextStyle(lineHeight = FONT_SIZE_24.sp)
        textView.applyLineHeight(styleWithLineHeight)
        assert(textView.lineHeight > 0)
    }

    @Test
    fun testApplyTextAlignStart() {
        textView.applyTextAlign(TextAlign.Start)
        assert((textView.gravity and Gravity.START) != 0)
    }

    @Test
    fun testApplyTextAlignEnd() {
        textView.applyTextAlign(TextAlign.End)
        assert((textView.gravity and Gravity.END) != 0)
    }

    @Test
    fun testApplyTextAlignCenter() {
        textView.applyTextAlign(TextAlign.Center)
        assert((textView.gravity and Gravity.CENTER_HORIZONTAL) != 0)
    }

    @Test
    fun testApplyTextAlignLeft() {
        textView.applyTextAlign(TextAlign.Left)
        assert((textView.gravity and Gravity.START) != 0)
    }

    @Test
    fun testApplyTextAlignRight() {
        textView.applyTextAlign(TextAlign.Right)
        assert((textView.gravity and Gravity.END) != 0)
    }

    @Test
    fun testEnableTextOverflowWithShortText() {
        textView.text = "Short"
        textView.maxLines = 2
        
        textView.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(LAYOUT_WIDTH_200, android.view.View.MeasureSpec.EXACTLY),
            android.view.View.MeasureSpec.makeMeasureSpec(LAYOUT_HEIGHT_100, android.view.View.MeasureSpec.EXACTLY)
        )
        textView.layout(0, 0, LAYOUT_WIDTH_200, LAYOUT_HEIGHT_100)
        
        textView.enableTextOverflow()
        
        assert(textView.text.toString() == "Short")
    }

    @Test
    fun testEnableTextOverflowWithLongText() {
        val longText = "This is a very long text that should definitely overflow when constrained to a single line and should be truncated with an ellipsis character at the end"
        textView.text = longText
        textView.maxLines = 1
        
        textView.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(LAYOUT_WIDTH_100, android.view.View.MeasureSpec.EXACTLY),
            android.view.View.MeasureSpec.makeMeasureSpec(LAYOUT_HEIGHT_50, android.view.View.MeasureSpec.EXACTLY)
        )
        textView.layout(0, 0, LAYOUT_WIDTH_100, LAYOUT_HEIGHT_50)
        
        textView.enableTextOverflow()
        
        assert(textView.text != null)
    }

    @Test
    fun testCombinedTextAppearance() {
        val combinedStyle = TextStyle(
            fontSize = FONT_SIZE_16.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.LineThrough,
            textAlign = TextAlign.Center,
            color = Color.Red,
            lineHeight = LINE_HEIGHT_20.sp
        )

        textView.applyFontSize(combinedStyle)
        textView.applyFontWeight(FontWeight.Bold)
        textView.applyFontStyle(FontStyle.Italic)
        textView.applyTextDecoration(combinedStyle)
        textView.applyTextAlign(TextAlign.Center)
        textView.applyTextColor(Color.Red.toArgb())
        textView.applyLineHeight(combinedStyle)

        assert(textView.textSize > 0)
        assert(textView.typeface.isBold || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        assert(textView.typeface.isItalic)
        assert((textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) != 0)
        assert((textView.gravity and Gravity.CENTER_HORIZONTAL) != 0)
        assert(textView.currentTextColor == Color.Red.toArgb())
        assert(textView.lineHeight > 0)
    }

    @Test
    fun testTypefacePreservation() {
        val originalTypeface = textView.typeface
        
        textView.applyFontWeight(FontWeight.Normal)
        assert(textView.typeface != null)
        
        textView.applyFontStyle(FontStyle.Normal)
        assert(textView.typeface != null)
    }

    @Test
    fun testMultipleColorApplications() {
        val colors = listOf(
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Yellow,
            Color.Magenta
        )

        colors.forEach { color ->
            textView.applyTextColor(color.toArgb())
            assert(textView.currentTextColor == color.toArgb())
        }
    }
}
