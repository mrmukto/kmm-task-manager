package com.mrm.taskmanager.taskmanager.data.domain.model

import kotlinx.datetime.Instant

data class Task(
    val id: Long? = null,              // null when creating a new task
    val title: String,
    val description: String? = null,
    val priority: Priority = Priority.MEDIUM,
    val status: TaskStatus = TaskStatus.TODO,
    val dueDate: Instant? = null,
    val createdAt: Instant,
)
