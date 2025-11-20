package com.mrm.taskmanager.taskmanager.data.model

import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus

data class TaskEntity(
    val id: Long,
    val title: String,
    val description: String?,
    val priority: Priority,
    val status: TaskStatus,
    val dueDate: kotlinx.datetime.Instant?,
    val createdAt: kotlinx.datetime.Instant,
)
