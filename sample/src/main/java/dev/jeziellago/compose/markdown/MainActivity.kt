package dev.jeziellago.compose.markdown

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jeziellago.compose.markdown.sample.R
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SampleMarkdown() }
    }
}

@Preview
@Composable
private fun SampleMarkdown() {
    val context = LocalContext.current

    MaterialTheme {
        Surface {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                item {
                    MarkdownText(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        markdown = """
                        ## Gif Images
                        ![Image](https://user-images.githubusercontent.com/14011726/94132137-7d4fc100-fe7c-11ea-8512-69f90cb65e48.gif)
                        
                        ---
                        """.trimIndent()
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                        ## Checks
                        - [x] Review #123
                        - [ ] Complete XYZ
                        - [ ] Add delight to the experience when all tasks are complete :tada:
                        
                        ---
                """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        syntaxHighlightColor = Color.Black,
                        syntaxHighlightTextColor = Color.White,
                        markdown = """
                        ## Text highlight
                        Sometimes we need to ==highlight== a text in `different` ways.
                        
                        ---
                """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                        <h2>Head with heading break color blue</h2>
                        
                        ---
                """.trimIndent(),
                        headingBreakColor = Color.Blue
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Custom font

                            This text is using OpenSans Regular. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                            
                            ---
                        
                        """.trimIndent(),
                        fontResource = R.font.opensans_regular,
                        modifier = Modifier.clickable {
                            Toast
                                .makeText(context, "On text click", Toast.LENGTH_SHORT)
                                .show()
                        },
                        style = TextStyle(
                            textAlign = TextAlign.Justify,
                            lineHeight = 24.sp,
                            fontSize = 18.sp
                        )
                    )
                }
                item {
                    MarkdownText(
                        modifier = Modifier.clickable {
                            Toast
                                .makeText(context, "On text click", Toast.LENGTH_SHORT)
                                .show()
                        },
                        disableLinkMovementMethod = true,
                        markdown = """
                            ## Clickable item

                            Should appear a single toast when clicking on this item!
                            
                            ---
                        
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                        ## Selectable item
                       
                       This is a selectable text. You can select this text by long pressing on it.
                       
                """.trimIndent(),
                        isTextSelectable = true
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Justify

                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                        
                        """.trimIndent(),
                        style = TextStyle(
                            textAlign = TextAlign.Justify,
                        )
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Color
                            
                            This text should appears in Blue color.
                            
                        """.trimIndent(),
                        style = TextStyle(
                            color = Color.Blue
                        )
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                           ---
                            __Advertisement :)__
                            
                            - __[nodeca](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
                              resize in browser.
                            - __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
                              i18n with plurals support and easy syntax.
                            
                            You will like those projects!
                            
                            ---
                        """.trimIndent(),
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(onLongPress = {
                                Toast
                                    .makeText(context, "On long press", Toast.LENGTH_SHORT)
                                    .show()
                            })
                        })
                }
                item {
                    MarkdownText(
                        markdown = """
                            # h1 Heading 8-)
                            ## h2 Heading
                            ### h3 Heading
                            #### h4 Heading
                            ##### h5 Heading
                            ###### h6 Heading
                            
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Horizontal Rules

                            ___
                            
                            ---
                            
                            ***
                            
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Emphasis

                            **This is bold text**
                            
                            __This is bold text__
                            
                            *This is italic text*
                            
                            _This is italic text_
                            
                            ~~Strikethrough~~
                            
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Links

                            [link text](http://dev.nodeca.com)
                            
                            [link with title](http://nodeca.github.io/pica/demo/ "title text!")
                            
                            Autoconverted link https://github.com/nodeca/pica (enable linkify to see)
                            
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        modifier = Modifier.fillMaxWidth(),
                        markdown = """
                            ## Images

                            ![Minion](https://octodex.github.com/images/minion.png)
                            ![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        modifier = Modifier.fillMaxWidth(),
                        markdown = """
                            Content
                            with
                            line
                            break

                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        modifier = Modifier.fillMaxWidth(),
                        markdown = """
                            ## Tables

                            | Option | Description |
                            | ------ | ----------- |
                            | data   | path to data files to supply the data that will be passed into templates. |
                            | engine | engine to be used for processing templates. Handlebars is the default. |
                            | ext    | extension to be used for dest files. |
                            
                            Right aligned columns
                            
                            | Option | Description |
                            | ------:| -----------:|
                            | data   | path to data files to supply the data that will be passed into templates. |
                            | engine | engine to be used for processing templates. Handlebars is the default. |
                            | ext    | extension to be used for dest files. |
                            
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Lists

                            Unordered
                            
                            + Create a list by starting a line with `+`, `-`, or `*`
                            + Sub-lists are made by indenting 2 spaces:
                                - Marker character change forces new list start:
                                    * Ac tristique libero volutpat at
                                    + Facilisis in pretium nisl aliquet
                                    - Nulla volutpat aliquam velit
                            + Very easy!
                            
                            Ordered
                            
                            1. Lorem ipsum dolor sit amet
                            2. Consectetur adipiscing elit
                            3. Integer molestie lorem at massa
                            
                            
                            1. You can use sequential numbers...
                            1. ...or keep all the numbers as `1.`
                            
                            Start numbering with offset:
                            
                            57. foo
                            1. bar

                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Blockquotes

                            > Blockquotes can also be nested...
                            >> ...by using additional greater-than signs right next to each other...
                            > > > ...or with spaces between arrows.

                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = """
                            ## Code

                            Inline `code`
                            
                            Indented code
                            
                                // Some comments
                                line 1 of code
                                line 2 of code
                                line 3 of code
                            
                            
                            Block code "fences"
                            
                            ```
                            Sample text here...
                            ```
                            
                            more code here..
                            
                            ``` js
                            var foo = function (bar) {
                              return bar++;
                            };
                            
                            console.log(foo(5));
                            ```
                        """.trimIndent(),
                    )
                }
                item {
                    MarkdownText(
                        markdown = "\n# HTML SECTION ${
                            String(
                                context.resources.openRawResource(R.raw.html).readBytes()
                            )
                        }",
                    )
                }
            }
        }
    }
}
