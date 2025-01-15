package dev.jeziellago.compose.markdown

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jeziellago.compose.markdown.sample.R
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SampleMarkdown() }
    }

    @Composable
    fun SampleMarkdown() {
        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            item {
                MarkdownText(
                    modifier = Modifier.animateContentSize(),
                    markdown = stringResource(id = R.string.test1),
                    style = MinorThird.editStyle(
                        MinorThird.bodySmall,
                        color = (colorResource(R.color.white))
                    )
                )
            }
            item {
                MarkdownText(
                    modifier = Modifier.animateContentSize(),
                    markdown = stringResource(id = R.string.test1),
                    style = MinorThird.editStyle(
                        MinorThird.bodySmall,
                        fontSize = 34.sp,
                        color = (colorResource(R.color.white))
                    )
                )
            }
            item {
                MarkdownText(
                    modifier = Modifier.animateContentSize(),
                    markdown = stringResource(id = R.string.test1),
                    style = MinorThird.editStyle(
                        MinorThird.bodySmall,
                        fontSize = 14.sp,
                        color = (colorResource(R.color.white))
                    )
                )
            }
        }
    }
}
