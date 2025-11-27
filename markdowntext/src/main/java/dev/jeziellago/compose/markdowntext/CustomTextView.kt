package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.withTranslation
import io.noties.markwon.core.spans.BlockQuoteSpan
import io.noties.markwon.core.spans.CodeBlockSpan
import io.noties.markwon.ext.tables.TableRowSpan
import io.noties.markwon.ext.tables.TableSpan
import kotlin.math.ceil

/**
 * This View contains a hack of the original TextView to fix the sizing issue of multiline text.
 * When a text has multiple lines, the TextView forcefully sets the width to match_parent, even if
 * the text layout does not span the whole width.
 *
 * The code comes from this article:
 * https://medium.com/@mxdiland/android-textview-multiline-problem-61f8c3499bbb
 */
class CustomTextView : AppCompatTextView {
    private enum class ExplicitLayoutAlignment {
        LEFT, CENTER, RIGHT
    }

    private var extraPaddingRight: Int? = null
    private var isTextSelectable: Boolean = false
    var wrapMultilineTextWidth: Boolean = true

    constructor(context: Context) :
            super(context, null, android.R.attr.textViewStyle)

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs, android.R.attr.textViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!layout.shouldWrap()) return

        val maxLineWidth = ceil(getMaxLineWidth(layout)).toInt()
        val uselessPaddingWidth = layout.width - maxLineWidth

        val width = measuredWidth - uselessPaddingWidth
        val height = measuredHeight
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (!layout.shouldWrap()) {
            super.onDraw(canvas)
            return
        }

        val layoutWidth = layout.width
        val maxLineWidth = ceil(getMaxLineWidth(layout)).toInt()
        if (layoutWidth == maxLineWidth) {
            super.onDraw(canvas)
            return
        }

        val explicitLayoutAlignment = when (layout.alignment) {
            Layout.Alignment.ALIGN_CENTER -> ExplicitLayoutAlignment.CENTER

            Layout.Alignment.ALIGN_NORMAL ->
                if (layoutDirection == LAYOUT_DIRECTION_LTR) ExplicitLayoutAlignment.LEFT
                else ExplicitLayoutAlignment.RIGHT

            Layout.Alignment.ALIGN_OPPOSITE ->
                if (layoutDirection == LAYOUT_DIRECTION_LTR) ExplicitLayoutAlignment.RIGHT
                else ExplicitLayoutAlignment.LEFT
        }

        val dx = when (explicitLayoutAlignment) {
            ExplicitLayoutAlignment.RIGHT -> -1 * (layoutWidth - maxLineWidth)
            ExplicitLayoutAlignment.CENTER -> -1 * (layoutWidth - maxLineWidth) / 2
            else -> 0
        }
        drawTranslatedHorizontally(canvas, dx) { super.onDraw(it) }
    }

    override fun getCompoundPaddingRight(): Int =
        extraPaddingRight ?: super.getCompoundPaddingRight()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        if (isTextSelectable) {
            return super.onTouchEvent(event)
        } else {
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_DOWN) {
                val link = getClickableSpans(event)

                if (link.isNotEmpty()) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        link[0].onClick(this)
                    }
                    return true
                }
            }
            return false
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (selectionStart < 0 || selectionEnd < 0) {
            (text as? Spannable)?.let {
                Selection.setSelection(it, it.length)
            }
        } else if (selectionStart != selectionEnd) {
            if (event?.actionMasked == MotionEvent.ACTION_DOWN) {
                val text = getText()
                setText(null)
                setText(text)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun getClickableSpans(event: MotionEvent): Array<ClickableSpan> {
        var x = event.x.toInt()
        var y = event.y.toInt()

        x -= totalPaddingLeft
        y -= totalPaddingTop

        x += scrollX
        y += scrollY

        val layout = layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val spannable = text as Spannable
        return spannable.getSpans(off, off, ClickableSpan::class.java)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun setTextIsSelectable(selectable: Boolean) {
        super.setTextIsSelectable(selectable)
        isTextSelectable = selectable
    }

    private fun getMaxLineWidth(layout: Layout): Float =
        (0 until layout.lineCount).maxOfOrNull { layout.getLineWidth(it) } ?: 0.0f

    private fun drawTranslatedHorizontally(canvas: Canvas, dx: Int, onDraw: (Canvas) -> Unit) {
        extraPaddingRight = dx
        canvas.withTranslation(dx.toFloat(), 0f) {
            onDraw.invoke(this)
            extraPaddingRight = null
        }
    }

    private fun Layout?.shouldWrap(): Boolean {
        return this != null && lineCount > 1 && wrapMultilineTextWidth && !containsLongMarkdown()
    }

    private fun containsLongMarkdown(): Boolean {
        // Do not wrap width when displaying markers needing full width (tables etc...)
        val spannable = if (text is Spannable) text as Spannable else SpannableString(text)
        return spannable.getSpans(0, text.length, Object::class.java).any {
            it is TableRowSpan || it is TableSpan || it is CodeBlockSpan || it is BlockQuoteSpan
        }
    }
}
