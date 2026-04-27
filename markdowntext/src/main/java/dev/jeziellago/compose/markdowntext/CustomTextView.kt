package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View.MeasureSpec
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context: Context) : AppCompatTextView(context) {

    private var isTextSelectable: Boolean = false
    private var lastMeasureWidth = -1
    private var onBlockClick: (() -> Unit)? = null
    private var areLinkClicksEnabled: Boolean = true
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var downX = 0f
    private var downY = 0f
    private var hasMoved = false
    private var didLongPress = false
    private var didPerformClickForCurrentGesture = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                hasMoved = false
                didLongPress = false
                didPerformClickForCurrentGesture = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!hasMoved) {
                    val deltaX = kotlin.math.abs(event.x - downX)
                    val deltaY = kotlin.math.abs(event.y - downY)
                    hasMoved = deltaX > touchSlop || deltaY > touchSlop
                }
            }
        }

        if (areLinkClicksEnabled &&
            (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_DOWN)
        ) {
            val link = getClickableSpans(event)

            if (link.isNotEmpty()) {
                if (event.action == MotionEvent.ACTION_UP) {
                    link[0].onClick(this)
                }
                return true
            }
        }

        if (isTextSelectable) {
            val handledBySuper = super.onTouchEvent(event)
            if (event.actionMasked == MotionEvent.ACTION_UP &&
                !didLongPress &&
                !hasMoved &&
                selectionStart == selectionEnd &&
                !didPerformClickForCurrentGesture
            ) {
                performClick()
                return true
            }
            return handledBySuper
        }

        return false
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
        didPerformClickForCurrentGesture = true
        val handledBySuper = super.performClick()
        onBlockClick?.invoke()
        return handledBySuper || onBlockClick != null
    }

    override fun performLongClick(): Boolean {
        didLongPress = true
        return super.performLongClick()
    }

    override fun setTextIsSelectable(selectable: Boolean) {
        super.setTextIsSelectable(selectable)
        isTextSelectable = selectable
    }

    fun setOnBlockClickListener(listener: (() -> Unit)?) {
        onBlockClick = listener
    }

    fun setLinkClicksEnabled(enabled: Boolean) {
        areLinkClicksEnabled = enabled
    }
}
