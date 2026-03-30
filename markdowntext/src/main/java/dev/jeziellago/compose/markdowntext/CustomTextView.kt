package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View.MeasureSpec
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context: Context) : AppCompatTextView(context) {

    private var isTextSelectable: Boolean = false
    private var lastMeasureWidth = -1

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)

        // If width changed significantly, recalculate layout
        // This fixes rendering issues in LazyColumn where views are recycled
        if (lastMeasureWidth != width && width > 0) {
            lastMeasureWidth = width
            invalidate()
            requestLayout()
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up resources when view is recycled in LazyColumn
        // This prevents text corruption when views are reused
        resetTextState()
    }

    fun removeAllSpans() {
        val text = text
        if (text is Spannable) {
            val spans = text.getSpans(0, text.length, Any::class.java)
            for (span in spans) {
                (text as? Spannable)?.removeSpan(span)
            }
        }
    }

    fun resetTextState() {
        text = ""
        removeAllSpans()
        lastMeasureWidth = -1
    }

    private fun getClickableSpans(event: MotionEvent): Array<ClickableSpan> {
        var x = event.x.toInt()
        var y = event.y.toInt()

        x -= totalPaddingLeft
        y -= totalPaddingTop

        x += scrollX
        y += scrollY

        val layout = layout ?: return emptyArray()

        if (y < 0 || y >= layout.height) {
            return emptyArray()
        }

        val line = layout.getLineForVertical(y)
        if (line < 0 || line >= layout.lineCount) {
            return emptyArray()
        }

        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val spannable = text as? Spannable ?: return emptyArray()
        return spannable.getSpans(off, off, ClickableSpan::class.java)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun setTextIsSelectable(selectable: Boolean) {
        super.setTextIsSelectable(selectable)
        isTextSelectable = selectable
    }
}
