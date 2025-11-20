package com.mrm.taskmanager.taskmanager.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.ui.theme.StatusDoneGreen
import com.mrm.taskmanager.taskmanager.ui.theme.StatusInProgressYellow
import com.mrm.taskmanager.taskmanager.ui.theme.SurfaceLight
import com.mrm.taskmanager.taskmanager.ui.theme.todoColor

@Composable
fun StatusChip(status: TaskStatus) {
    val (bg, fg, label) = when (status) {
        TaskStatus.TODO -> Triple(
            todoColor,
            Color.White,
            "To Do"
        )
        TaskStatus.IN_PROGRESS -> Triple(
            StatusInProgressYellow,
            Color.White,
            "In Progress"
        )
        TaskStatus.DONE -> Triple(
            StatusDoneGreen,
            Color.White,
            "Done"
        )
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = bg,
        tonalElevation = 0.dp
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
