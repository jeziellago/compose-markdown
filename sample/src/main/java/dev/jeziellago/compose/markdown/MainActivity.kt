package dev.jeziellago.compose.markdown

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleMarkdown()
        }
    }

    @Composable
    fun SampleMarkdown() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 10.dp
        ) {
            LazyColumn(
                modifier = Modifier.padding(8.dp)
            ) {
                item {
                    MarkdownText(markdown = demo, viewId = R.id.markdownTextId, onClick = {
                        Toast.makeText(this@MainActivity, "On text click", Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }
}