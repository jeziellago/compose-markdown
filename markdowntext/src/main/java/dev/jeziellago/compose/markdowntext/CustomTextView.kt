package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.accessibility.AccessibilityEvent
import android.view.MotionEvent
import android.view.View.MeasureSpec
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.withTranslation
import androidx.core.text.getSpans
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import io.noties.markwon.core.spans.BlockQuoteSpan
import io.noties.markwon.core.spans.CodeBlockSpan
import io.noties.markwon.ext.tables.TableRowSpan
import io.noties.markwon.ext.tables.TableSpan
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

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
    var wrapMultilineTextWidth: Boolean = false
    private var lastMeasureWidth = -1
    private var onBlockClick: (() -> Unit)? = null
    private var areLinkClicksEnabled: Boolean = true
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var downX = 0f
    private var downY = 0f
    private var hasMoved = false
    private var didLongPress = false
    private var didPerformClickForCurrentGesture = false
    private var isBlockLevelAccessibilityEnabled: Boolean = false
    private var blockAccessibilityHelper: BlockAccessibilityHelper? = null

    private data class AccessibilityBlock(
        val id: Int,
        val firstLine: Int,
        val lastLine: Int,
        val bounds: Rect,
        val text: CharSequence,
        val clickableSpan: ClickableSpan? = null,
    )

    init {
        blockAccessibilityHelper = BlockAccessibilityHelper(this)
        ViewCompat.setAccessibilityDelegate(this, blockAccessibilityHelper)
    }

    constructor(context: Context) :
            super(context, null, android.R.attr.textViewStyle)

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs, android.R.attr.textViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)

        // If width changed significantly, recalculate layout
        // This fixes rendering issues in LazyColumn where views are recycled
        if (lastMeasureWidth != measuredWidth && measuredWidth > 0) {
            lastMeasureWidth = measuredWidth
            invalidate()
            requestLayout()
            return
        }

        if (!layout.shouldWrap()) return

        val maxLineWidth = ceil(getMaxLineWidth(layout)).toInt()
        val uselessPaddingWidth = layout.width - maxLineWidth
        val wrappedWidth = measuredWidth - uselessPaddingWidth
        val height = measuredHeight
        setMeasuredDimension(wrappedWidth, height)
    }

    override fun dispatchHoverEvent(event: MotionEvent): Boolean {
        return (blockAccessibilityHelper?.dispatchHoverEvent(event) == true) || super.dispatchHoverEvent(event)
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

            // Default for Java null
            else -> ExplicitLayoutAlignment.LEFT
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

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        blockAccessibilityHelper?.invalidateRoot()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            blockAccessibilityHelper?.invalidateRoot()
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
        blockAccessibilityHelper?.invalidateRoot()
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
        val directClickableSpans = spannable.getSpans(off, off, ClickableSpan::class.java)
        if (directClickableSpans.isNotEmpty()) {
            return directClickableSpans
        }

        // Table cells render clickable spans inside row-internal layouts.
        val tableRowSpans = spannable.getSpans(off, off, TableRowSpan::class.java)
        if (tableRowSpans.isEmpty()) {
            return emptyArray()
        }

        val tableRowSpan = tableRowSpans[0]
        val rowLayout = tableRowSpan.findLayoutForHorizontalOffset(x) ?: return emptyArray()
        val rowY = layout.getLineTop(line)
        val rowRelativeY = y - rowY
        if (rowRelativeY < 0 || rowRelativeY >= rowLayout.height) {
            return emptyArray()
        }

        val rowLine = rowLayout.getLineForVertical(rowRelativeY)
        if (rowLine < 0 || rowLine >= rowLayout.lineCount) {
            return emptyArray()
        }

        val cellWidth = tableRowSpan.cellWidth()
        if (cellWidth <= 0) {
            return emptyArray()
        }

        // Map touch coordinates into the tapped table cell layout.
        val rowOff = rowLayout.getOffsetForHorizontal(rowLine, (x % cellWidth).toFloat())
        val rowText = rowLayout.text as? Spanned ?: return emptyArray()
        return rowText.getSpans(rowOff, rowOff, ClickableSpan::class.java)
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
        blockAccessibilityHelper?.invalidateRoot()
    }

    fun setLinkClicksEnabled(enabled: Boolean) {
        if (areLinkClicksEnabled == enabled) return
        areLinkClicksEnabled = enabled
        blockAccessibilityHelper?.invalidateRoot()
    }

    fun setBlockLevelAccessibilityEnabled(enabled: Boolean) {
        if (isBlockLevelAccessibilityEnabled == enabled) return
        isBlockLevelAccessibilityEnabled = enabled
        blockAccessibilityHelper?.invalidateRoot()
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
        return spannable.getSpans<Any>(0, text.length).any {
            it is TableRowSpan || it is TableSpan || it is CodeBlockSpan || it is BlockQuoteSpan
        }
    }

    private fun buildAccessibilityBlocks(): List<AccessibilityBlock> {
        val layout = layout ?: return emptyList()
        val content = text?.toString().orEmpty()
        if (content.isEmpty() || layout.lineCount == 0) return emptyList()

        val blocks = mutableListOf<AccessibilityBlock>()
        val spanned = text as? Spanned
        var runStartLine = -1
        var runStartOffset = -1

        fun isLineBlank(line: Int): Boolean {
            val start = layout.getLineStart(line).coerceIn(0, content.length)
            val end = layout.getLineEnd(line).coerceIn(0, content.length)
            if (start >= end) return true
            return content.substring(start, end).trim().isEmpty()
        }

        fun lineEndsWithExplicitNewline(line: Int): Boolean {
            val end = layout.getLineEnd(line).coerceIn(0, content.length)
            if (end <= 0) return false
            return content[end - 1] == '\n'
        }

        fun closeRun(lastLine: Int, runEndOffset: Int) {
            if (runStartLine == -1 || runStartOffset == -1) return
            val safeStartOffset = runStartOffset.coerceIn(0, content.length)
            val safeEndOffset = runEndOffset.coerceIn(0, content.length)
            val rawBlockText = content.substring(safeStartOffset.coerceIn(0, safeEndOffset), safeEndOffset)
            val blockText = blockTextForAccessibility(rawBlockText, safeStartOffset, safeEndOffset)
            if (blockText.isEmpty()) {
                runStartLine = -1
                runStartOffset = -1
                return
            }

            val left = (totalPaddingLeft - scrollX).coerceAtLeast(0)
            val right = (width - totalPaddingRight - scrollX).coerceAtLeast(left + 1)
            val top = (totalPaddingTop + layout.getLineTop(runStartLine) - scrollY).coerceAtLeast(0)
            val bottom = (totalPaddingTop + layout.getLineBottom(lastLine) - scrollY).coerceAtLeast(top + 1)

            blocks += AccessibilityBlock(
                id = blocks.size,
                firstLine = runStartLine,
                lastLine = lastLine,
                bounds = Rect(left, top, right, bottom),
                text = blockText,
            )
            runStartLine = -1
            runStartOffset = -1
        }

        for (line in 0 until layout.lineCount) {
            val lineStart = layout.getLineStart(line).coerceIn(0, content.length)
            val lineEnd = layout.getLineEnd(line).coerceIn(0, content.length)
            val tableRowSpan = spanned
                ?.getSpans(lineStart, lineEnd, TableRowSpan::class.java)
                ?.firstOrNull()

            if (tableRowSpan != null) {
                closeRun(
                    lastLine = line - 1,
                    runEndOffset = lineStart,
                )
                addTableAccessibilityBlocks(blocks, layout, line, tableRowSpan)
                continue
            }

            if (isLineBlank(line)) {
                closeRun(
                    lastLine = line - 1,
                    runEndOffset = lineStart,
                )
                continue
            }

            if (runStartLine == -1) {
                runStartLine = line
                runStartOffset = lineStart
            }

            if (lineEndsWithExplicitNewline(line) && line < layout.lineCount - 1) {
                closeRun(
                    lastLine = line,
                    runEndOffset = layout.getLineEnd(line).coerceIn(0, content.length),
                )
            }
        }

        if (runStartLine != -1) {
            val lastLine = layout.lineCount - 1
            closeRun(
                lastLine = lastLine,
                runEndOffset = layout.getLineEnd(lastLine).coerceIn(0, content.length),
            )
        }

        return blocks
    }

    private fun addTableAccessibilityBlocks(
        blocks: MutableList<AccessibilityBlock>,
        layout: Layout,
        line: Int,
        tableRowSpan: TableRowSpan,
    ) {
        val cellWidth = tableRowSpan.cellWidth()
        if (cellWidth <= 0) {
            blocks += AccessibilityBlock(
                id = blocks.size,
                firstLine = line,
                lastLine = line,
                bounds = tableRowBounds(layout, line),
                text = "Table row",
            )
            return
        }

        var cellIndex = 0
        while (true) {
            val cellLayout = tableRowSpan.findLayoutForHorizontalOffset(cellIndex * cellWidth) ?: break
            val cellText = cellLayout.text
            val spannedCellText = cellText as? Spanned
            val cellBounds = tableCellBounds(layout, line, cellIndex, cellWidth)
            val clickableSpans = if (areLinkClicksEnabled) {
                spannedCellText
                    ?.getSpans(0, cellText.length, ClickableSpan::class.java)
                    ?.sortedBy { spannedCellText.getSpanStart(it) }
                    .orEmpty()
            } else {
                emptyList()
            }

            val trimmedStart = cellText.indexOfFirst { !it.isWhitespace() }
            val trimmedEnd = cellText.indexOfLast { !it.isWhitespace() } + 1
            val singleLinkCoversCell = clickableSpans.singleOrNull()?.let {
                trimmedStart >= 0 &&
                    (spannedCellText?.getSpanStart(it) ?: Int.MAX_VALUE) <= trimmedStart &&
                    (spannedCellText?.getSpanEnd(it) ?: Int.MIN_VALUE) >= trimmedEnd
            } == true

            if (!singleLinkCoversCell) {
                val label = cellText.toString().trim()
                if (label.isNotEmpty()) {
                    blocks += AccessibilityBlock(
                        id = blocks.size,
                        firstLine = line,
                        lastLine = line,
                        bounds = cellBounds,
                        text = label,
                    )
                }
            }

            for (clickableSpan in clickableSpans) {
                val spanStart = spannedCellText?.getSpanStart(clickableSpan) ?: continue
                val spanEnd = spannedCellText.getSpanEnd(clickableSpan)
                if (spanStart < 0 || spanEnd <= spanStart) continue

                blocks += AccessibilityBlock(
                    id = blocks.size,
                    firstLine = line,
                    lastLine = line,
                    bounds = tableLinkBounds(cellLayout, cellBounds, spanStart, spanEnd),
                    text = cellText.subSequence(spanStart, spanEnd).toString(),
                    clickableSpan = clickableSpan,
                )
            }
            cellIndex++
        }
    }

    private fun tableRowBounds(layout: Layout, line: Int): Rect {
        val left = (totalPaddingLeft - scrollX).coerceAtLeast(0)
        val right = (width - totalPaddingRight - scrollX).coerceAtLeast(left + 1)
        val top = (totalPaddingTop + layout.getLineTop(line) - scrollY).coerceAtLeast(0)
        val bottom = (totalPaddingTop + layout.getLineBottom(line) - scrollY).coerceAtLeast(top + 1)
        return Rect(left, top, right, bottom)
    }

    private fun tableCellBounds(layout: Layout, line: Int, cellIndex: Int, cellWidth: Int): Rect {
        val rowBounds = tableRowBounds(layout, line)
        val left = (rowBounds.left + cellIndex * cellWidth).coerceAtMost(rowBounds.right - 1)
        val right = (left + cellWidth).coerceAtMost(rowBounds.right).coerceAtLeast(left + 1)
        return Rect(left, rowBounds.top, right, rowBounds.bottom)
    }

    private fun tableLinkBounds(
        cellLayout: Layout,
        cellBounds: Rect,
        spanStart: Int,
        spanEnd: Int,
    ): Rect {
        val safeStart = spanStart.coerceIn(0, cellLayout.text.length)
        val safeEnd = spanEnd.coerceIn(safeStart + 1, cellLayout.text.length)
        val firstLine = cellLayout.getLineForOffset(safeStart)
        val lastLine = cellLayout.getLineForOffset(safeEnd - 1)
        var left = Float.MAX_VALUE
        var right = -Float.MAX_VALUE

        for (line in firstLine..lastLine) {
            val lineStart = cellLayout.getLineStart(line)
            val lineEnd = cellLayout.getLineEnd(line)
            val segmentStart = max(safeStart, lineStart)
            val segmentEnd = min(safeEnd, lineEnd)
            val startX = if (segmentStart == lineStart) {
                cellLayout.getLineLeft(line)
            } else {
                cellLayout.getPrimaryHorizontal(segmentStart)
            }
            val endX = if (segmentEnd == lineEnd) {
                cellLayout.getLineRight(line)
            } else {
                cellLayout.getPrimaryHorizontal(segmentEnd)
            }
            left = min(left, min(startX, endX))
            right = max(right, max(startX, endX))
        }

        val horizontalInset = ((cellBounds.width() - cellLayout.width) / 2).coerceAtLeast(0)
        val verticalInset = ((cellBounds.height() - cellLayout.height) / 2).coerceAtLeast(0)
        val linkBounds = Rect(
            cellBounds.left + horizontalInset + left.toInt(),
            cellBounds.top + verticalInset + cellLayout.getLineTop(firstLine),
            cellBounds.left + horizontalInset + right.toInt(),
            cellBounds.top + verticalInset + cellLayout.getLineBottom(lastLine),
        )
        if (!linkBounds.intersect(cellBounds)) return cellBounds
        return if (linkBounds.width() > 0 && linkBounds.height() > 0) linkBounds else cellBounds
    }

    private fun blockTextForAccessibility(rawText: String, startOffset: Int, endOffset: Int): String {
        val trimmed = rawText.trim()
        if (trimmed.isNotEmpty()) return trimmed

        val spanned = text as? Spanned ?: return ""
        if (spanned.getSpans(startOffset, endOffset, TableRowSpan::class.java).isNotEmpty() ||
            spanned.getSpans(startOffset, endOffset, TableSpan::class.java).isNotEmpty()
        ) {
            return "Table row"
        }
        if (spanned.getSpans(startOffset, endOffset, CodeBlockSpan::class.java).isNotEmpty()) {
            return "Code block"
        }
        if (spanned.getSpans(startOffset, endOffset, BlockQuoteSpan::class.java).isNotEmpty()) {
            return "Block quote"
        }
        return ""
    }

    private fun shouldUseVirtualBlockAccessibility(blocks: List<AccessibilityBlock>): Boolean {
        if (!isBlockLevelAccessibilityEnabled) return false
        return blocks.isNotEmpty()
    }

    private inner class BlockAccessibilityHelper(host: CustomTextView) : ExploreByTouchHelper(host) {

        override fun onPopulateNodeForHost(node: AccessibilityNodeInfoCompat) {
            super.onPopulateNodeForHost(node)
            val blocks = buildAccessibilityBlocks()
            if (shouldUseVirtualBlockAccessibility(blocks)) {
                node.text = null
                node.contentDescription = null
                node.className = android.view.View::class.java.name
                node.isFocusable = false
                node.isClickable = false
                node.isScreenReaderFocusable = false
            }
        }

        override fun getVirtualViewAt(x: Float, y: Float): Int {
            val blocks = buildAccessibilityBlocks()
            if (!shouldUseVirtualBlockAccessibility(blocks)) return INVALID_ID
            if (x < 0f || y < 0f || x >= width.toFloat() || y >= height.toFloat()) {
                return INVALID_ID
            }

            val layout = layout ?: return INVALID_ID
            if (layout.height <= 0) return INVALID_ID

            val tappedBlock = blocks.firstOrNull {
                it.clickableSpan != null && it.bounds.contains(x.toInt(), y.toInt())
            } ?: blocks.firstOrNull { it.bounds.contains(x.toInt(), y.toInt()) }
            if (tappedBlock != null) return tappedBlock.id

            val localY = (y.toInt() + scrollY - totalPaddingTop).coerceIn(0, layout.height - 1)
            val line = layout.getLineForVertical(localY)
            return blocks.firstOrNull { line in it.firstLine..it.lastLine }?.id ?: INVALID_ID
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>) {
            val blocks = buildAccessibilityBlocks()
            if (!shouldUseVirtualBlockAccessibility(blocks)) return
            for (block in blocks) {
                virtualViewIds += block.id
            }
        }

        override fun onPopulateNodeForVirtualView(
            virtualViewId: Int,
            node: AccessibilityNodeInfoCompat,
        ) {
            val blocks = buildAccessibilityBlocks()
            if (!shouldUseVirtualBlockAccessibility(blocks)) {
                node.setBoundsInParent(Rect(0, 0, 1, 1))
                node.contentDescription = ""
                node.isVisibleToUser = false
                return
            }
            val block = blocks.firstOrNull { it.id == virtualViewId } ?: run {
                node.setBoundsInParent(Rect(0, 0, 1, 1))
                node.contentDescription = ""
                node.isVisibleToUser = false
                return
            }

            node.className = AppCompatTextView::class.java.name
            node.packageName = context.packageName
            node.setBoundsInParent(block.bounds)
            node.text = block.text
            node.isFocusable = true
            node.isVisibleToUser = block.bounds.bottom > 0 && block.bounds.top < height
            if (block.clickableSpan != null && areLinkClicksEnabled) {
                node.roleDescription = "link"
                node.isClickable = true
                node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK)
            } else if (onBlockClick != null) {
                node.isClickable = true
                node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK)
            }
        }

        override fun onPerformActionForVirtualView(
            virtualViewId: Int,
            action: Int,
            arguments: android.os.Bundle?,
        ): Boolean {
            val blocks = buildAccessibilityBlocks()
            if (!shouldUseVirtualBlockAccessibility(blocks)) return false
            val block = blocks.firstOrNull { it.id == virtualViewId } ?: return false
            if (action != AccessibilityNodeInfoCompat.ACTION_CLICK) return false

            if (block.clickableSpan != null && areLinkClicksEnabled) {
                block.clickableSpan.onClick(this@CustomTextView)
                sendEventForVirtualView(virtualViewId, AccessibilityEvent.TYPE_VIEW_CLICKED)
                return true
            }
            if (onBlockClick != null) {
                onBlockClick?.invoke()
                sendEventForVirtualView(virtualViewId, AccessibilityEvent.TYPE_VIEW_CLICKED)
                return true
            }
            return false
        }
    }
}
