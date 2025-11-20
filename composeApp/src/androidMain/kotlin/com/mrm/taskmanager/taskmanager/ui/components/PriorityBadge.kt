package com.mrm.taskmanager.taskmanager.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.ui.theme.ErrorRed
import com.mrm.taskmanager.taskmanager.ui.theme.PrimaryLight
import com.mrm.taskmanager.taskmanager.ui.theme.SurfaceLight

@Composable
fun PriorityBadge(priority: Priority) {
    val (bg, fg, label) = when (priority) {
        Priority.LOW -> Triple(
            SurfaceLight,
            Color.Black,
            "Low"
        )
        Priority.MEDIUM -> Triple(
            PrimaryLight,
            Color.White,
            "Medium"
        )
        Priority.HIGH -> Triple(
            ErrorRed,
            Color.White,
            "High"
        )
    }
    Surface(
        shape = MaterialTheme.shapes.small,
        color = bg,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(fg)
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = fg
            )
        }
    }
}
