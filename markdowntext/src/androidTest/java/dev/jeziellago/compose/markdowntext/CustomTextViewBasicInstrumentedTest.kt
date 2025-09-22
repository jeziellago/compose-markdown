package dev.jeziellago.compose.markdowntext

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomTextViewBasicInstrumentedTest {

    companion object {
        private const val LAYOUT_WIDTH_300 = 300
        private const val LAYOUT_HEIGHT_100 = 100
    }

    private lateinit var customTextView: CustomTextView

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        customTextView = CustomTextView(context)
    }

    @Test
    fun testSetTextIsSelectable() {
        customTextView.setTextIsSelectable(true)
        assert(customTextView.isTextSelectable)
        
        customTextView.setTextIsSelectable(false)
        assert(!customTextView.isTextSelectable)
    }

    @Test
    fun testTextViewProperties() {
        customTextView.text = "Test text"
        assert(customTextView.text.toString() == "Test text")
        
        customTextView.setTextColor(android.graphics.Color.RED)
        assert(customTextView.currentTextColor == android.graphics.Color.RED)
    }

    @Test
    fun testLayoutAndMeasurement() {
        customTextView.text = "Test text for layout"
        
        customTextView.measure(
            View.MeasureSpec.makeMeasureSpec(LAYOUT_WIDTH_300, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(LAYOUT_HEIGHT_100, View.MeasureSpec.EXACTLY)
        )
        
        assert(customTextView.measuredWidth == LAYOUT_WIDTH_300)
        assert(customTextView.measuredHeight == LAYOUT_HEIGHT_100)
        
        customTextView.layout(0, 0, LAYOUT_WIDTH_300, LAYOUT_HEIGHT_100)
        
        assert(customTextView.width == LAYOUT_WIDTH_300)
        assert(customTextView.height == LAYOUT_HEIGHT_100)
    }
}
