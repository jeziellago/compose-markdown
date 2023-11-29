package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.annotation.IdRes
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnNextLayout
import androidx.core.widget.TextViewCompat
import coil.ImageLoader
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin

/**
 * Requires API Level 26 to apply auto size
 * */
data class AutoSizeConfig(
    val autoSizeMinTextSize: Int,
    val autoSizeMaxTextSize: Int,
    val autoSizeStepGranularity: Int,
    val unit: Int = TypedValue.COMPLEX_UNIT_SP,
)

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    linkColor: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    truncateOnTextOverflow: Boolean = false,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    isTextSelectable: Boolean = false,
    autoSizeConfig: AutoSizeConfig? = null,
    @FontRes fontResource: Int? = null,
    style: TextStyle = LocalTextStyle.current,
    @IdRes viewId: Int? = null,
    onClick: (() -> Unit)? = null,
    // this option will disable all clicks on links, inside the markdown text
    // it also enable the parent view to receive the click event
    disableLinkMovementMethod: Boolean = false,
    imageLoader: ImageLoader? = null,
    linkifyMask: Int = Linkify.EMAIL_ADDRESSES or Linkify.PHONE_NUMBERS or Linkify.WEB_URLS,
    onLinkClicked: ((String) -> Unit)? = null,
    onTextLayout: ((numLines: Int) -> Unit)? = null
) {
    val defaultColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    val context: Context = LocalContext.current
    val markdownRender: Markwon =
        remember { createMarkdownRender(context, imageLoader, linkifyMask, onLinkClicked) }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            createTextView(
                context = ctx,
                color = color,
                linkColor = linkColor,
                defaultColor = defaultColor,
                fontSize = fontSize,
                fontResource = fontResource,
                maxLines = maxLines,
                isTextSelectable = isTextSelectable,
                autoSizeConfig = autoSizeConfig,
                style = style,
                textAlign = textAlign,
                truncateOnTextOverflow = truncateOnTextOverflow,
                lineHeight = lineHeight,
                viewId = viewId,
                onClick = onClick,
            )
        },
        update = { textView ->
            markdownRender.setMarkdown(textView, markdown)
            if (disableLinkMovementMethod) {
                textView.movementMethod = null
            }
            if (onTextLayout != null) {
                textView.post {
                    onTextLayout(textView.lineCount)
                }
            }
            textView.maxLines = maxLines
        }
    )
}

private fun createTextView(
    context: Context,
    color: Color = Color.Unspecified,
    linkColor: Color = Color.Unspecified,
    defaultColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    truncateOnTextOverflow: Boolean = false,
    lineHeight: TextUnit,
    maxLines: Int = Int.MAX_VALUE,
    isTextSelectable: Boolean = false,
    autoSizeConfig: AutoSizeConfig? = null,
    @FontRes fontResource: Int? = null,
    style: TextStyle,
    @IdRes viewId: Int? = null,
    onClick: (() -> Unit)? = null
): TextView {

    val textColor = color.takeOrElse { style.color.takeOrElse { defaultColor } }
    val linkTextColor = linkColor.takeOrElse { style.color.takeOrElse { defaultColor } }
    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
            textAlign = textAlign,
            lineHeight = if (lineHeight != TextUnit.Unspecified) lineHeight else style.lineHeight,
        )
    )
    return TextView(context).apply {
        onClick?.let { setOnClickListener { onClick() } }
        setTextColor(textColor.toArgb())
        setLinkTextColor(linkTextColor.toArgb())
        when {
            style.lineHeight.isSp ->
                TextViewCompat.setLineHeight(
                    this,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        style.lineHeight.value,
                        context.resources.displayMetrics
                    ).toInt()
                )
        }
        setMaxLines(maxLines)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, mergedStyle.fontSize.value)

        setTextIsSelectable(isTextSelectable)
        movementMethod = LinkMovementMethod.getInstance()

        viewId?.let { id = viewId }
        textAlign?.let { align ->
            textAlignment = when (align) {
                TextAlign.Left, TextAlign.Start -> View.TEXT_ALIGNMENT_TEXT_START
                TextAlign.Right, TextAlign.End -> View.TEXT_ALIGNMENT_TEXT_END
                TextAlign.Center -> View.TEXT_ALIGNMENT_CENTER
                else -> View.TEXT_ALIGNMENT_TEXT_START
            }
        }

        if (lineHeight != TextUnit.Unspecified) {
            setLineSpacing(lineHeight.value, 1f)
        }

        if (mergedStyle.textDecoration == TextDecoration.LineThrough) {
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        fontResource?.let { font ->
            typeface = ResourcesCompat.getFont(context, font)
        }

        if (truncateOnTextOverflow) {
            doOnNextLayout {
                if (maxLines != -1 && lineCount > maxLines) {
                    val endOfLastLine = layout.getLineEnd(maxLines - 1)
                    val spannedDropLast3Chars = text.subSequence(0, endOfLastLine - 3) as? Spanned
                    if (spannedDropLast3Chars != null) {
                        val spannableBuilder = SpannableStringBuilder()
                            .append(spannedDropLast3Chars)
                            .append("…")

                        text = spannableBuilder
                    }
                }
            }
        }

        autoSizeConfig?.let { config ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setAutoSizeTextTypeUniformWithConfiguration(
                    config.autoSizeMinTextSize,
                    config.autoSizeMaxTextSize,
                    config.autoSizeStepGranularity,
                    config.unit
                )
            }
        }
    }
}

private fun createMarkdownRender(
    context: Context,
    imageLoader: ImageLoader?,
    linkifyMask: Int,
    onLinkClicked: ((String) -> Unit)? = null
): Markwon {
    val coilImageLoader = imageLoader ?: ImageLoader.Builder(context)
        .apply {
            crossfade(true)
        }.build()

    return Markwon.builder(context)
        .usePlugin(HtmlPlugin.create())
        .usePlugin(CoilImagesPlugin.create(context, coilImageLoader))
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(TablePlugin.create(context))
        .usePlugin(LinkifyPlugin.create(linkifyMask))
        .usePlugin(SoftBreakAddsNewLinePlugin.create())
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                // Setting [MarkwonConfiguration.Builder.linkResolver] overrides
                // Markwon's default behaviour - see Markwon's [LinkResolverDef]
                // and how it's used in [MarkwonConfiguration.Builder].
                // Only use it if the client explicitly wants to handle link clicks.
                onLinkClicked ?: return
                builder.linkResolver { _, link ->
                    // handle individual clicks on Textview link
                    onLinkClicked.invoke(link)
                }
            }
        })
        .build()
}