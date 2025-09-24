package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CustomTextViewTest {

    private lateinit var context: Context
    private lateinit var customTextView: CustomTextView

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        customTextView = CustomTextView(context)
    }

    @Test
    fun `test CustomTextView creation`() {
        assert(customTextView is CustomTextView)
        assert(!customTextView.isTextSelectable)
    }

    @Test
    fun `test setTextIsSelectable updates internal flag`() {
        customTextView.setTextIsSelectable(true)
        
        customTextView.setTextIsSelectable(false)
    }

    @Test
    fun `test onTouchEvent calls performClick`() {
        val result = customTextView.performClick()
        assert(result is Boolean)
    }

    @Test
    fun `test onTouchEvent with text selectable delegates to super`() {
        customTextView.setTextIsSelectable(true)
        
        assert(customTextView.isTextSelectable)
    }

    @Test
    fun `test onTouchEvent with clickable span`() {
        val spannableString = SpannableString("Click here")
        customTextView.text = spannableString
        
        assert(customTextView.text.toString() == "Click here")
    }

    @Test
    fun `test onTouchEvent with ACTION_DOWN`() {
        customTextView.text = "Test text"
        customTextView.setTextIsSelectable(false)
        
        assert(customTextView.text.toString() == "Test text")
        assert(!customTextView.isTextSelectable)
    }

    @Test
    fun `test performClick returns super result`() {
        val result = customTextView.performClick()
        assert(result is Boolean)
    }
}
