package dev.jeziellago.compose.markdowntext

import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.font.LoadedFontFamily
import androidx.compose.ui.text.font.SystemFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.content.res.ResourcesCompat
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

//TODO: should implement the font loading by setting up the typeface on native TextView
fun TextView.applyFontFamily(fontFamily: FontFamily) {
    when(fontFamily) {
        is SystemFontFamily -> {}
        is FontListFontFamily -> {}
        is LoadedFontFamily -> {}
    }
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
    textAlignment = when (align) {
        TextAlign.Left, TextAlign.Start -> View.TEXT_ALIGNMENT_TEXT_START
        TextAlign.Right, TextAlign.End -> View.TEXT_ALIGNMENT_TEXT_END
        TextAlign.Center -> View.TEXT_ALIGNMENT_CENTER
        else -> View.TEXT_ALIGNMENT_TEXT_START
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && align == TextAlign.Justify) {
        justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    }
}

fun TextView.enableTextOverflow() {
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
