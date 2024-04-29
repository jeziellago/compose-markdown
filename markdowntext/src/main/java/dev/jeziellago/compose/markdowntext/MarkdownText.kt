package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.os.Build
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import io.noties.markwon.Markwon

@Deprecated(message = "The parameters `color`, `fontSize`, `textAlign` and `lineHeight` must be part of TextStyle.")
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
    disableLinkMovementMethod: Boolean = false,
    imageLoader: ImageLoader? = null,
    linkifyMask: Int = Linkify.EMAIL_ADDRESSES or Linkify.PHONE_NUMBERS or Linkify.WEB_URLS,
    enableSoftBreakAddsNewLine: Boolean = true,
    onLinkClicked: ((String) -> Unit)? = null,
    onTextLayout: ((numLines: Int) -> Unit)? = null
) {

    val mergedStyle = style.merge(
        TextStyle(
            color = color,
            fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
            textAlign = textAlign,
            lineHeight = if (lineHeight != TextUnit.Unspecified) lineHeight else style.lineHeight,
        )
    )

    MarkdownText(
        markdown = markdown,
        modifier = modifier,
        linkColor = linkColor,
        truncateOnTextOverflow = truncateOnTextOverflow,
        maxLines = maxLines,
        isTextSelectable = isTextSelectable,
        autoSizeConfig = autoSizeConfig,
        fontResource = fontResource,
        style = mergedStyle,
        viewId = viewId,
        onClick = onClick,
        disableLinkMovementMethod = disableLinkMovementMethod,
        imageLoader = imageLoader,
        linkifyMask = linkifyMask,
        enableSoftBreakAddsNewLine = enableSoftBreakAddsNewLine,
        onLinkClicked = onLinkClicked,
        onTextLayout = onTextLayout,
    )
}

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    linkColor: Color = Color.Unspecified,
    truncateOnTextOverflow: Boolean = false,
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
    enableSoftBreakAddsNewLine: Boolean = true,
    onLinkClicked: ((String) -> Unit)? = null,
    onTextLayout: ((numLines: Int) -> Unit)? = null
) {
    val defaultColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    val context: Context = LocalContext.current
    val markdownRender: Markwon =
        remember {
            MarkdownRender.create(
                context,
                imageLoader,
                linkifyMask,
                enableSoftBreakAddsNewLine,
                onLinkClicked
            )
        }

    AndroidView(
        modifier = modifier,
        factory = { factoryContext ->

            val linkTextColor = linkColor.takeOrElse { style.color.takeOrElse { defaultColor } }

            TextView(factoryContext).apply {
                viewId?.let { id = viewId }
                onClick?.let { setOnClickListener { onClick() } }
                fontResource?.let { font -> applyFontResource(font) }

                setMaxLines(maxLines)
                setLinkTextColor(linkTextColor.toArgb())

                setTextIsSelectable(isTextSelectable)

                movementMethod = LinkMovementMethod.getInstance()

                if (truncateOnTextOverflow) enableTextOverflow()

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
        },
        update = { textView ->
            with(textView) {
                applyTextColor(style.color.takeOrElse { defaultColor }.toArgb())
                applyFontSize(style)
                applyLineHeight(style)
                applyLineSpacing(style)
                applyTextDecoration(style)

                with(style) {
                    textAlign?.let { applyTextAlign(it) }
                    fontStyle?.let { applyFontStyle(it) }
                    fontWeight?.let { applyFontWeight(it) }
                }
            }
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
