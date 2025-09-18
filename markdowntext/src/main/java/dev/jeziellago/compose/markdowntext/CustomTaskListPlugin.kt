package dev.jeziellago.compose.markdowntext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.noties.markwon.ext.tasklist.TaskListPlugin

object CustomTaskListPlugin {
    fun create(
        checkboxColor: Color,
        checkedCheckboxColor: Color,
        checkmarkColor: Color = Color.White
    ): TaskListPlugin {
        return TaskListPlugin.create(
            checkedCheckboxColor.toArgb(),
            checkboxColor.toArgb(),
            checkmarkColor.toArgb()
        )
    }
}
