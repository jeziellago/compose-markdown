package dev.jeziellago.compose.markdowntext

import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.font.resolveAsTypeface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.doOnNextLayout
import androidx.core.widget.TextViewCompat

fun TextView.applyFontWeight(fontWeight: FontWeight) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        typeface = Typeface.create(typeface, fontWeight.weight, false)
    } else {
        val weight = when (fontWeight) {
            ExtraBold, Bold, SemiBold -> Typeface.BOLD
            else -> Typeface.NORMAL
        }
        setTypeface(typeface, weight)
    }
}

fun TextView.applyFontStyle(fontStyle: FontStyle) {
    val type = when (fontStyle) {
        FontStyle.Italic -> Typeface.ITALIC
        FontStyle.Normal -> Typeface.NORMAL
        else -> Typeface.NORMAL
    }
    setTypeface(typeface, type)
}

fun TextView.applyFontFamily(textStyle: TextStyle) {
    typeface = createFontFamilyResolver(context).resolveAsTypeface(
        fontFamily = textStyle.fontFamily,
        fontWeight = textStyle.fontWeight ?: FontWeight.Normal,
        fontStyle = textStyle.fontStyle ?: FontStyle.Normal,
        fontSynthesis = textStyle.fontSynthesis ?: FontSynthesis.All,
    ).value
}

fun TextView.applyFontResource(@FontRes font: Int) {
    typeface = ResourcesCompat.getFont(context, font)
}

fun TextView.applyTextColor(argbColor: Int) {
    setTextColor(argbColor)
}

fun TextView.applyFontSize(textStyle: TextStyle) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, textStyle.fontSize.value)
}

fun TextView.applyTextDecoration(textStyle: TextStyle) {
    if (textStyle.textDecoration == TextDecoration.LineThrough) {
        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    }
}

fun TextView.applyLineHeight(textStyle: TextStyle) {
    if (textStyle.lineHeight.isSp) {
        TextViewCompat.setLineHeight(
            this,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                textStyle.lineHeight.value,
                context.resources.displayMetrics
            ).toInt()
        )
    }
}

fun TextView.applyTextAlign(align: TextAlign) {
    gravity = when (align) {
        TextAlign.Left, TextAlign.Start -> Gravity.START
        TextAlign.Right, TextAlign.End -> Gravity.END
        TextAlign.Center -> Gravity.CENTER_HORIZONTAL
        else -> Gravity.START
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && align == TextAlign.Justify) {
        justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    }
}

fun TextView.applyTextSelectionColors(textSelectionColors: TextSelectionColors) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val handleColor = textSelectionColors.handleColor.toArgb()
        textSelectHandleLeft?.let { DrawableCompat.setTint(it, handleColor) }
        textSelectHandleRight?.let { DrawableCompat.setTint(it, handleColor) }
        textSelectHandle?.let { DrawableCompat.setTint(it, handleColor) }
    }

    highlightColor = textSelectionColors.backgroundColor.toArgb()
}

fun TextView.enableTextOverflow() {
    doOnNextLayout {
        try {
            if (maxLines != -1 && lineCount > maxLines) {
                val layout = layout ?: return@doOnNextLayout
                
                // Get the end index of the max lines allowed
                val lineEndIndex = layout.getLineEnd(maxLines - 1)
                
                // Validate indices - prevent StringIndexOutOfBoundsException
                if (lineEndIndex < 0 || lineEndIndex > text.length) {
                    return@doOnNextLayout
                }
                
                // Calculate safe truncation point accounting for ellipsis
                val ellipsisWidth = paint.measureText("…")
                var endIndex = lineEndIndex
                
                // Move back character by character until ellipsis fits
                while (endIndex > 0) {
                    val currentWidth = paint.measureText(text.subSequence(0, endIndex).toString())
                    val availableWidth = layout.getLineWidth(maxLines - 1)
                    
                    if (currentWidth + ellipsisWidth <= availableWidth) {
                        break
                    }
                    endIndex--
                }
                
                // Ensure minimum content is kept (at least 1 character)
                if (endIndex <= 0) {
                    endIndex = 1
                }
                
                // Safely truncate text and append ellipsis
                val truncatedText = text.subSequence(0, endIndex) as? Spanned
                if (truncatedText != null) {
                    val spannableBuilder = SpannableStringBuilder()
                        .append(truncatedText)
                        .append("…")
                    text = spannableBuilder
                }
            }
        } catch (_: Exception) {
            // Fallback: keep original text if any error occurs during truncation
        }
    }
}

