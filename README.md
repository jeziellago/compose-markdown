# Compose Markdown

[![](https://androidweekly.net/issues/issue-456/badge)](https://androidweekly.net/issues/issue-456)
[![CI](https://github.com/jeziellago/compose-markdown/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jeziellago/compose-markdown/actions/workflows/ci.yml)
[![Android Tests](https://github.com/jeziellago/compose-markdown/actions/workflows/android-test.yml/badge.svg?branch=main)](https://github.com/jeziellago/compose-markdown/actions/workflows/android-test.yml)
[![JitPack](https://jitpack.io/v/jeziellago/compose-markdown.svg)](https://jitpack.io/#jeziellago/compose-markdown)

Render Markdown inside Jetpack Compose with a single composable.

This library wraps a `TextView`-based renderer in Compose so you can display rich Markdown content while still using familiar Compose modifiers and text styling.

[![Video]()](https://github.com/jeziellago/compose-markdown/assets/8452419/0e17e3d9-4eb1-44cb-8b63-5056fe74395e)

## Features

- CommonMark-style markdown rendering
- HTML support
- Remote images and GIFs
- Tables
- Task lists
- Linkify support for URLs, emails, and phone numbers
- Text highlighting with `==highlight==`
- Click handling for the whole composable or individual links
- Text selection support
- Optional font resource and auto-size support

## Installation

### 1. Add JitPack

In your root `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.jeziellago:compose-markdown:VERSION'
}
```

## Basic Usage

```kotlin
import androidx.compose.runtime.Composable
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun ArticleBody() {
    val markdown = """
        # Compose Markdown

        Render **markdown**, [links](https://github.com/jeziellago/compose-markdown),
        images, tables, task lists, and even inline HTML.

        - [x] Markdown
        - [x] HTML
        - [x] Images
    """.trimIndent()

    MarkdownText(markdown = markdown)
}
```

## Styled Usage

`MarkdownText` supports many of the same styling controls you would typically use with Compose text.

```kotlin
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun StyledMarkdown() {
    MarkdownText(
        modifier = Modifier.padding(16.dp),
        markdown = """
            ## Styled markdown

            This text uses a custom `TextStyle`, supports [links](https://example.com),
            and can be limited to a specific number of lines.
        """.trimIndent(),
        maxLines = 4,
        style = TextStyle(
            color = Color(0xFF1F3A5F),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Justify,
        ),
    )
}
```

## Supported Content

The sample app demonstrates support for:

- Headings
- Paragraphs
- Bold, italic, and strikethrough text
- Inline code and fenced code blocks
- Blockquotes
- Ordered and unordered lists
- Horizontal rules
- Tables
- HTML content
- Remote images and animated GIFs
- Task lists
- Autolinked URLs, emails, and phone numbers
- Highlighted text with `==highlight==`

## Useful Options

Some of the most useful parameters exposed by `MarkdownText`:

- `modifier`: apply Compose layout and interaction modifiers
- `style`: configure color, alignment, font size, line height, and more
- `maxLines`: limit rendered lines
- `truncateOnTextOverflow`: enable ellipsizing
- `fontResource`: apply an Android font resource
- `isTextSelectable`: enable long-press text selection
- `linkColor`: override link color
- `disableLinkMovementMethod`: disable internal link handling so parent click handlers can receive taps
- `onClick`: handle clicks on the whole composable
- `onLinkClicked`: intercept markdown link clicks
- `imageLoader`: provide a custom Coil `ImageLoader`
- `autoSizeConfig`: enable auto-sizing on API 26+
- `enableSoftBreakAddsNewLine`: treat soft breaks as new lines
- `headingBreakColor`: customize heading divider color
- `enableUnderlineForLink`: turn link underlines on or off
- `onTextLayout`: observe rendered line count

## Advanced Example

```kotlin
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun InteractiveMarkdown() {
    val context = LocalContext.current

    MarkdownText(
        markdown = """
            ## Interactive content

            Visit [GitHub](https://github.com/jeziellago/compose-markdown)
            or tap anywhere in this block.
        """.trimIndent(),
        style = TextStyle(fontSize = 18.sp),
        isTextSelectable = true,
        onClick = {
            Toast.makeText(context, "Block clicked", Toast.LENGTH_SHORT).show()
        },
        onLinkClicked = { url ->
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
        },
    )
}
```

## Sample App

The repository includes a `sample` app showing real usage for:

- GIF rendering
- task lists
- syntax highlight styling
- heading divider customization
- custom fonts
- selectable text
- clickable containers
- tables, lists, quotes, code blocks, and HTML rendering

Run it with:

```bash
./gradlew :sample:installDebug
```

## Development

Useful commands while working on the project:

```bash
./gradlew test
./gradlew :markdowntext:testDebugUnitTest
./gradlew :markdowntext:connectedDebugAndroidTest
```

## Contributors

Thank you all for your contribution.

<a href="https://github.com/jeziellago/compose-markdown/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=jeziellago/compose-markdown" />
</a>

## Stargazers Over Time

[![Stargazers over time](https://starchart.cc/jeziellago/compose-markdown.svg?variant=adaptive)](https://starchart.cc/jeziellago/compose-markdown)

## License

MIT License

```text
Copyright (c) 2021 Jeziel Lago

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
