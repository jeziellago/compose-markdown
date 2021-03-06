package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import coil.ImageLoader
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    @FontRes fontResource: Int? = null,
    style: TextStyle = LocalTextStyle.current
) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        }
    }
    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            textAlign = textAlign,
            letterSpacing = letterSpacing
        )
    )

    val context = LocalContext.current
    val markdownRender = remember { createMarkdownRender(context) }
    val markdownText = TextView(LocalContext.current).apply {
        setTextColor(textColor.toArgb())
        setMaxLines(maxLines)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, mergedStyle.fontSize.value)
        if(mergedStyle.letterSpacing.type != TextUnitType.Unspecified) setLetterSpacing(mergedStyle.letterSpacing.value)
        when (textAlign) {
            TextAlign.Left -> View.TEXT_ALIGNMENT_TEXT_START
            TextAlign.Right -> View.TEXT_ALIGNMENT_TEXT_END
            TextAlign.Center -> View.TEXT_ALIGNMENT_CENTER
            TextAlign.Justify -> null
            TextAlign.Start -> View.TEXT_ALIGNMENT_TEXT_START
            TextAlign.End -> View.TEXT_ALIGNMENT_TEXT_END
            null -> null
        }?.let {
            textAlignment = it
        }
        if (fontResource != null) {
            val typeface = ResourcesCompat.getFont(context, fontResource)
            setTypeface(typeface)
        }


    }

    AndroidView(modifier = modifier, factory = { markdownText })
    markdownRender.setMarkdown(markdownText, markdown)
}


private fun createMarkdownRender(context: Context): Markwon {
    val imageLoader = ImageLoader.Builder(context)
        .apply {
            availableMemoryPercentage(0.5)
            bitmapPoolPercentage(0.5)
            crossfade(true)
        }
        .build()

    return Markwon.builder(context)
        .usePlugin(HtmlPlugin.create())
        .usePlugin(CoilImagesPlugin.create(context, imageLoader))
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(LinkifyPlugin.create())
        .build()
}
