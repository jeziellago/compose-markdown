package dev.jeziellago.compose.markdowntext

import android.graphics.Paint
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkdownTextViewInstrumentedTest {

    companion object {
        private const val FONT_SIZE_16 = 16
        private const val FONT_SIZE_18 = 18
        private const val FONT_SIZE_20 = 20
        private const val MAX_LINES_2 = 2
        private const val AUTO_SIZE_MIN = 10
        private const val AUTO_SIZE_MAX = 20
        private const val AUTO_SIZE_STEP = 2
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBasicTextViewCreation() {
        val markdown = "Hello World"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("basic_text")
                )
            }
        }

        composeTestRule.onNodeWithTag("basic_text").assertExists()
        composeTestRule.onNodeWithTag("basic_text").assertIsDisplayed()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Hello World")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTextViewWithCustomColor() {
        val markdown = "Colored text"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("colored_text"),
                    style = TextStyle(color = Color.Red)
                )
            }
        }

        composeTestRule.onNodeWithTag("colored_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Colored text")))
            .check(matches(allOf(
                isDisplayed(),
                hasTextColor(Color.Red.toArgb())
            )))
    }

    @Test
    fun testTextViewWithCustomFontSize() {
        val markdown = "Large text"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("large_text"),
                    style = TextStyle(fontSize = FONT_SIZE_20.sp)
                )
            }
        }

        composeTestRule.onNodeWithTag("large_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Large text")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTextViewMaxLines() {
        val markdown = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("max_lines_text"),
                    maxLines = MAX_LINES_2
                )
            }
        }

        composeTestRule.onNodeWithTag("max_lines_text").assertExists()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasMaxLines(MAX_LINES_2)
            )))
    }

    @Test
    fun testTextViewSelectable() {
        val markdown = "Selectable text"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("selectable_text"),
                    isTextSelectable = true
                )
            }
        }

        composeTestRule.onNodeWithTag("selectable_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Selectable text")))
            .check(matches(allOf(
                isDisplayed(),
                isTextSelectable()
            )))
    }

    @Test
    fun testTextViewWithStrikethrough() {
        val markdown = "Strikethrough text"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("strikethrough_text"),
                    style = TextStyle(textDecoration = TextDecoration.LineThrough)
                )
            }
        }

        composeTestRule.onNodeWithTag("strikethrough_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Strikethrough text")))
            .check(matches(allOf(
                isDisplayed(),
                hasStrikethroughFlag()
            )))
    }

    @Test
    fun testTextViewWithAutoSize() {
        val markdown = "Auto-sized text"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("auto_size_text"),
                    autoSizeConfig = AutoSizeConfig(
                        autoSizeMinTextSize = AUTO_SIZE_MIN,
                        autoSizeMaxTextSize = AUTO_SIZE_MAX,
                        autoSizeStepGranularity = AUTO_SIZE_STEP
                    )
                )
            }
        }

        composeTestRule.onNodeWithTag("auto_size_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Auto-sized text")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTextViewClickable() {
        val markdown = "Clickable text"
        var clicked = false
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("clickable_text"),
                    onClick = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("clickable_text").assertExists()
        composeTestRule.onNodeWithTag("clickable_text").performClick()
        
        assert(clicked)
    }

    @Test
    fun testMarkdownHeadingRendering() {
        val markdown = "# Heading Text"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("heading_text")
                )
            }
        }

        composeTestRule.onNodeWithTag("heading_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Heading Text")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testMarkdownListRendering() {
        val markdown = "- Item 1\n- Item 2"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("list_text")
                )
            }
        }

        composeTestRule.onNodeWithTag("list_text").assertExists()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testMarkdownLinkRendering() {
        val markdown = "[Link](https://example.com)"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("link_text"),
                    linkColor = Color.Blue
                )
            }
        }

        composeTestRule.onNodeWithTag("link_text").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Link")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTextViewPropertiesAfterRendering() {
        val markdown = "Test content"
        
        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.testTag("properties_test"),
                    style = TextStyle(
                        fontSize = FONT_SIZE_18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = MAX_LINES_2,
                    isTextSelectable = true
                )
            }
        }

        composeTestRule.onNodeWithTag("properties_test").assertExists()
        
        onView(allOf(isAssignableFrom(TextView::class.java), withText("Test content")))
            .check(matches(allOf(
                isDisplayed(),
                hasMaxLines(MAX_LINES_2),
                isTextSelectable()
            )))
    }

    private fun hasTextColor(expectedColor: Int): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has text color $expectedColor")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return textView.currentTextColor == expectedColor
            }
        }
    }

    private fun hasMaxLines(expectedMaxLines: Int): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has max lines of $expectedMaxLines")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return textView.maxLines == expectedMaxLines
            }
        }
    }

    private fun isTextSelectable(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("is text selectable")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return textView.isTextSelectable
            }
        }
    }

    private fun hasStrikethroughFlag(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has strikethrough paint flag")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return (textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) != 0
            }
        }
    }

    private fun hasTextContent(expectedText: String): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has text content containing '$expectedText'")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return textView.text.toString().contains(expectedText)
            }
        }
    }

    private fun isDisplayedAndNotEmpty(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("is displayed and has non-empty text")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return textView.visibility == android.view.View.VISIBLE && 
                       textView.text.isNotEmpty()
            }
        }
    }
}
