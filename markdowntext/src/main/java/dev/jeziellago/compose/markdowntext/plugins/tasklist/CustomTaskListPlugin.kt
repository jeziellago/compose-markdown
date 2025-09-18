package dev.jeziellago.compose.markdowntext.plugins.tasklist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.text.Spanned
import android.text.style.ReplacementSpan
import androidx.annotation.NonNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.SpanFactory
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.ext.tasklist.TaskListProps
import org.commonmark.node.Node

object CustomTaskListPlugin {
    fun create(
        context: Context,
        checkboxColor: Color,
        checkedCheckboxColor: Color
    ): MarkwonPlugin {
        // Use the TaskListPlugin.create overload that accepts custom colors
        return TaskListPlugin.create(
            checkedCheckboxColor.toArgb(),
            checkboxColor.toArgb(),
            android.graphics.Color.WHITE
        )
    }
}

class CustomTaskListSpansFactory(
    private val uncheckedColor: Int,
    private val checkedColor: Int
) : MarkwonSpansFactory {

object CustomTaskListPlugin {
    fun create(
        context: Context,
        checkboxColor: Color,
        checkedCheckboxColor: Color
    ): MarkwonPlugin {
        // Use the TaskListPlugin.create overload that accepts custom colors
        return TaskListPlugin.create(
            checkedCheckboxColor.toArgb(),
            checkboxColor.toArgb(),
            android.graphics.Color.WHITE 
        )
    }
}

    override fun <N : Node?> get(node: Class<N>): SpanFactory? {
        TODO("Not yet implemented")
    }

    override fun <N : Node?> require(node: Class<N>): SpanFactory {
        TODO("Not yet implemented")
    }
}

class CustomTaskListSpan(
    private val uncheckedColor: Int,
    private val checkedColor: Int,
    private val isDone: Boolean
) : ReplacementSpan() {
    
    private val checkboxSize = 48f // Size in pixels
    private val margin = 24f // Margin between checkbox and text
    
    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return (checkboxSize + margin).toInt()
    }
    
    override fun draw(
        @NonNull canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        @NonNull paint: Paint
    ) {
        val centerY = (top + bottom) / 2f
        val checkboxTop = centerY - checkboxSize / 2f
        val checkboxBottom = centerY + checkboxSize / 2f
        val checkboxRect = RectF(x, checkboxTop, x + checkboxSize, checkboxBottom)
        
        // Save original paint settings
        val originalColor = paint.color
        val originalStyle = paint.style
        val originalStrokeWidth = paint.strokeWidth
        
        // Draw checkbox border
        paint.color = if (isDone) checkedColor else uncheckedColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        canvas.drawRoundRect(checkboxRect, 6f, 6f, paint)
        
        if (isDone) {
            // Fill the checkbox if checked
            paint.style = Paint.Style.FILL
            paint.color = checkedColor
            canvas.drawRoundRect(checkboxRect, 6f, 6f, paint)
            
            // Draw checkmark
            paint.color = android.graphics.Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 6f
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeJoin = Paint.Join.ROUND
            
            val path = Path()
            val checkStartX = x + checkboxSize * 0.25f
            val checkMidX = x + checkboxSize * 0.45f
            val checkEndX = x + checkboxSize * 0.75f
            val checkStartY = centerY
            val checkMidY = centerY + checkboxSize * 0.15f
            val checkEndY = centerY - checkboxSize * 0.15f
            
            path.moveTo(checkStartX, checkStartY)
            path.lineTo(checkMidX, checkMidY)
            path.lineTo(checkEndX, checkEndY)
            
            canvas.drawPath(path, paint)
        }
        
        // Restore original paint settings
        paint.color = originalColor
        paint.style = originalStyle
        paint.strokeWidth = originalStrokeWidth
    }
}