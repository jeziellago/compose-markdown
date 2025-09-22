package dev.jeziellago.compose.markdowntext

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.style.*
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkdownComplexRenderingTest {

    companion object {
        private const val FONT_SIZE_14 = 14
        private const val FONT_SIZE_16 = 16
        private const val PADDING_16 = 16
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testComplexMarkdownDocument() {
        val complexMarkdown = """
            # Main Heading
            
            ## Subheading
            
            This is a paragraph with **bold text**, *italic text*, and `inline code`.
            
            ### Lists
            
            Unordered list:
            - First item
            - Second item
            - Third item
            
            Ordered list:
            1. First numbered item
            2. Second numbered item
            3. Third numbered item
            
            ### Links
            
            Visit [our website](https://example.com) for more information.
            
            ### Code Block
            
            ```kotlin
            fun hello() {
                println("Hello World")
            }
            ```
            
            ### Blockquote
            
            > This is a blockquote
            > with multiple lines
            
            ### Emphasis Combinations
            
            ***Bold and italic*** text combined.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = complexMarkdown,
                    modifier = Modifier
                        .testTag("complex_markdown")
                        .fillMaxWidth()
                        .padding(PADDING_16.dp),
                    style = TextStyle(fontSize = FONT_SIZE_14.sp)
                )
            }
        }

        composeTestRule.onNodeWithTag("complex_markdown").assertExists()
        composeTestRule.onNodeWithTag("complex_markdown").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText(),
                containsMarkdownElements()
            )))
    }

    @Test
    fun testTableRendering() {
        val tableMarkdown = """
            Simple table:
            
            | Header 1 | Header 2 |
            |----------|----------|
            | Cell 1   | Cell 2   |
            | Cell 3   | Cell 4   |
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = tableMarkdown,
                    modifier = Modifier
                        .testTag("table_test")
                        .fillMaxWidth()
                        .padding(PADDING_16.dp)
                )
            }
        }

        composeTestRule.onNodeWithTag("table_test").assertExists()
        composeTestRule.onNodeWithTag("table_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testImageMarkdownRendering() {
        val imageMarkdown = """
            # Image Test
            
            Here is an image:
            
            ![Alt text](https://example.com/image.jpg)
            
            And some text after the image.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = imageMarkdown,
                    modifier = Modifier.testTag("image_test"),
                    imageLoader = ImageLoader.Builder(InstrumentationRegistry.getInstrumentation().targetContext).build()
                )
            }
        }

        composeTestRule.onNodeWithTag("image_test").assertExists()
        composeTestRule.onNodeWithTag("image_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testLinksWithCallbacks() {
        val linkMarkdown = """
            Click on these links:
            - [Google](https://google.com)
            - [GitHub](https://github.com)
            - [Stack Overflow](https://stackoverflow.com)
        """.trimIndent()

        var lastClickedLink = ""

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = linkMarkdown,
                    modifier = Modifier.testTag("links_test"),
                    linkColor = Color.Blue,
                    onLinkClicked = { url -> lastClickedLink = url }
                )
            }
        }

        composeTestRule.onNodeWithTag("links_test").assertExists()
        composeTestRule.onNodeWithTag("links_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasClickableSpans()
            )))
    }

    @Test
    fun testCodeBlockWithSyntaxHighlighting() {
        val codeMarkdown = """
            Here's some Kotlin code:
            
            ```kotlin
            class Example {
                fun greet(name: String) {
                    println("Hello, ${'$'}name!")
                }
            }
            ```
            
            And some Python:
            
            ```python
            def hello(name):
                print(f"Hello, {name}!")
            ```
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = codeMarkdown,
                    modifier = Modifier.testTag("code_blocks"),
                    syntaxHighlightColor = Color.LightGray,
                    syntaxHighlightTextColor = Color.Black
                )
            }
        }

        composeTestRule.onNodeWithTag("code_blocks").assertExists()
        composeTestRule.onNodeWithTag("code_blocks").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testTaskListRendering() {
        val taskMarkdown = """
            # Task List
            
            - [x] Completed task
            - [ ] Incomplete task
            - [x] Another completed task
            - [ ] Another incomplete task
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = taskMarkdown,
                    modifier = Modifier.testTag("task_list")
                )
            }
        }

        composeTestRule.onNodeWithTag("task_list").assertExists()
        composeTestRule.onNodeWithTag("task_list").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testBlockquoteRendering() {
        val blockquoteMarkdown = """
            Regular text before blockquote.
            
            > This is a blockquote
            > that spans multiple lines
            > and should be visually distinct
            
            Regular text after blockquote.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = blockquoteMarkdown,
                    modifier = Modifier.testTag("blockquote_test")
                )
            }
        }

        composeTestRule.onNodeWithTag("blockquote_test").assertExists()
        composeTestRule.onNodeWithTag("blockquote_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testStrikethroughRendering() {
        val strikethroughMarkdown = """
            This text has ~~strikethrough~~ formatting.
            
            ~~Entire line strikethrough~~
            
            Normal text with ~~some crossed out~~ words.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = strikethroughMarkdown,
                    modifier = Modifier.testTag("strikethrough_test")
                )
            }
        }

        composeTestRule.onNodeWithTag("strikethrough_test").assertExists()
        composeTestRule.onNodeWithTag("strikethrough_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testNestedListRendering() {
        val nestedListMarkdown = """
            # Nested Lists
            
            - Main item 1
              - Sub item 1.1
              - Sub item 1.2
                - Sub sub item 1.2.1
            - Main item 2
              - Sub item 2.1
            - Main item 3
            
            Ordered with nesting:
            1. First main item
               1. First sub item
               2. Second sub item
            2. Second main item
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = nestedListMarkdown,
                    modifier = Modifier.testTag("nested_lists")
                )
            }
        }

        composeTestRule.onNodeWithTag("nested_lists").assertExists()
        composeTestRule.onNodeWithTag("nested_lists").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testMixedFormattingRendering() {
        val mixedMarkdown = """
            # Complex Formatting Test
            
            This paragraph contains **bold text**, *italic text*, ***bold italic***, 
            `inline code`, [links](https://example.com), and ~~strikethrough~~.
            
            > Blockquote with **bold** and *italic* text inside.
            
            - List item with `code`
            - List item with [link](https://test.com)
            - List item with **bold** text
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = mixedMarkdown,
                    modifier = Modifier.testTag("mixed_formatting"),
                    linkColor = Color.Blue,
                    syntaxHighlightColor = Color.Gray
                )
            }
        }

        composeTestRule.onNodeWithTag("mixed_formatting").assertExists()
        composeTestRule.onNodeWithTag("mixed_formatting").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testHorizontalRuleRendering() {
        val hrMarkdown = """
            Text before horizontal rule.
            
            ---
            
            Text after horizontal rule.
            
            Another section.
            
            ***
            
            Final section.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = hrMarkdown,
                    modifier = Modifier.testTag("horizontal_rules"),
                    headingBreakColor = Color.Gray
                )
            }
        }

        composeTestRule.onNodeWithTag("horizontal_rules").assertExists()
        composeTestRule.onNodeWithTag("horizontal_rules").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testEmailAndPhoneLinkification() {
        val linkifyMarkdown = """
            Contact Information:
            
            Email: test@example.com
            Phone: +1-555-123-4567
            Website: https://example.com
            
            Please reach out via any of these methods.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = linkifyMarkdown,
                    modifier = Modifier.testTag("linkify_test"),
                    linkifyMask = android.text.util.Linkify.ALL
                )
            }
        }

        composeTestRule.onNodeWithTag("linkify_test").assertExists()
        composeTestRule.onNodeWithTag("linkify_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testMarkdownWithCallbacks() {
        val callbackMarkdown = """
            # Callback Test
            
            Click on [this link](https://callback-test.com) to test callbacks.
            
            Some more content here.
        """.trimIndent()

        var beforeCalled = false
        var afterCalled = false
        var linkClicked = ""
        var layoutLines = 0

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = callbackMarkdown,
                    modifier = Modifier.testTag("callbacks_test"),
                    beforeSetMarkdown = { _, _ -> beforeCalled = true },
                    afterSetMarkdown = { _ -> afterCalled = true },
                    onLinkClicked = { url -> linkClicked = url },
                    onTextLayout = { lines -> layoutLines = lines }
                )
            }
        }

        composeTestRule.onNodeWithTag("callbacks_test").assertExists()
        composeTestRule.waitForIdle()
        
        assert(beforeCalled)
        assert(afterCalled)
        assert(layoutLines > 0)
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testSoftBreakConfiguration() {
        val softBreakMarkdown = """
            Line one
            Line two
            Line three
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = softBreakMarkdown,
                    modifier = Modifier.testTag("soft_break_test"),
                    enableSoftBreakAddsNewLine = true
                )
            }
        }

        composeTestRule.onNodeWithTag("soft_break_test").assertExists()
        composeTestRule.onNodeWithTag("soft_break_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testMarkdownWithCustomImageLoader() {
        val imageMarkdown = """
            # Images Test
            
            ![Local Image](file:///android_asset/test.png)
            
            ![Web Image](https://example.com/image.jpg)
            
            Text after images.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                val imageLoader = ImageLoader.Builder(context)
                    .crossfade(true)
                    .build()
                
                MarkdownText(
                    markdown = imageMarkdown,
                    modifier = Modifier.testTag("images_test"),
                    imageLoader = imageLoader
                )
            }
        }

        composeTestRule.onNodeWithTag("images_test").assertExists()
        composeTestRule.onNodeWithTag("images_test").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testComplexTableWithFormatting() {
        val complexTableMarkdown = """
            # Complex Table
            
            | **Bold Header** | *Italic Header* | `Code Header` |
            |-----------------|-----------------|---------------|
            | **Bold cell**   | *Italic cell*   | `Code cell`   |
            | Normal cell     | [Link cell](https://example.com) | ~~Strike cell~~ |
            
            Text after table.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = complexTableMarkdown,
                    modifier = Modifier
                        .testTag("complex_table")
                        .fillMaxWidth()
                        .padding(PADDING_16.dp),
                    linkColor = Color.Blue,
                    syntaxHighlightColor = Color.LightGray
                )
            }
        }

        composeTestRule.onNodeWithTag("complex_table").assertExists()
        composeTestRule.onNodeWithTag("complex_table").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    @Test
    fun testMarkdownWithAllFeatures() {
        val fullFeaturesMarkdown = """
            # Complete Markdown Test
            
            ## Text Formatting
            **Bold**, *italic*, ***bold italic***, `code`, ~~strikethrough~~
            
            ## Lists
            - Unordered item
            - Another item
            
            1. Ordered item
            2. Another ordered item
            
            ## Links and Images
            [Link](https://example.com)
            ![Image](https://example.com/img.jpg)
            
            ## Code
            ```
            Code block
            ```
            
            ## Quote
            > Blockquote text
            
            ## Table
            | A | B |
            |---|---|
            | 1 | 2 |
            
            ## Rule
            ---
            
            Final text.
        """.trimIndent()

        composeTestRule.setContent {
            MaterialTheme {
                MarkdownText(
                    markdown = fullFeaturesMarkdown,
                    modifier = Modifier
                        .testTag("full_features")
                        .fillMaxWidth()
                        .padding(PADDING_16.dp),
                    style = TextStyle(fontSize = FONT_SIZE_16.sp),
                    linkColor = Color.Blue,
                    syntaxHighlightColor = Color.Gray,
                    syntaxHighlightTextColor = Color.Black,
                    enableUnderlineForLink = true
                )
            }
        }

        composeTestRule.onNodeWithTag("full_features").assertExists()
        composeTestRule.onNodeWithTag("full_features").assertIsDisplayed()
        
        onView(isAssignableFrom(TextView::class.java))
            .check(matches(allOf(
                isDisplayed(),
                hasNonEmptyText()
            )))
    }

    private fun hasNonEmptyText(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has non-empty text")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                return textView.text.isNotEmpty()
            }
        }
    }

    private fun containsMarkdownElements(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("contains markdown elements")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text.toString()
                return text.contains("Main Heading") && 
                       text.contains("our website") &&
                       text.length > 100
            }
        }
    }

    private fun hasClickableSpans(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has clickable spans")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text
                if (text is Spannable) {
                    val spans = text.getSpans(0, text.length, ClickableSpan::class.java)
                    return spans.isNotEmpty()
                }
                return false
            }
        }
    }

    private fun hasCodeBlockSpans(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has code block spans")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text
                if (text is Spannable) {
                    val backgroundSpans = text.getSpans(0, text.length, BackgroundColorSpan::class.java)
                    return backgroundSpans.isNotEmpty()
                }
                return false
            }
        }
    }

    private fun hasQuoteSpans(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has quote spans")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text
                if (text is Spannable) {
                    val quoteSpans = text.getSpans(0, text.length, QuoteSpan::class.java)
                    return quoteSpans.isNotEmpty()
                }
                return false
            }
        }
    }

    private fun hasStrikethroughSpans(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has strikethrough spans")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text
                if (text is Spannable) {
                    val strikeSpans = text.getSpans(0, text.length, StrikethroughSpan::class.java)
                    return strikeSpans.isNotEmpty()
                }
                return false
            }
        }
    }

    private fun hasImageSpans(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image spans")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text
                if (text is Spannable) {
                    val imageSpans = text.getSpans(0, text.length, ImageSpan::class.java)
                    return imageSpans.isNotEmpty()
                }
                return false
            }
        }
    }

    private fun hasMultipleSpanTypes(): Matcher<android.view.View> {
        return object : BoundedMatcher<android.view.View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has multiple span types")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val text = textView.text
                if (text is Spannable) {
                    val allSpans = text.getSpans(0, text.length, Any::class.java)
                    val spanTypes = allSpans.map { it.javaClass }.distinct()
                    return spanTypes.size >= 2
                }
                return false
            }
        }
    }
}
