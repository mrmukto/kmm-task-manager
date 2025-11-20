package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus

enum class SortMode {
    CREATED_AT_ASC, CREATED_AT_DESC, PRIORITY, STATUS,
}

class SortTasksUseCase {

    operator fun invoke(
        tasks: List<Task>, sortMode: SortMode
    ): List<Task> {
        return when (sortMode) {
            SortMode.CREATED_AT_ASC -> tasks.sortedBy { it.createdAt }

            SortMode.CREATED_AT_DESC -> tasks.sortedByDescending { it.createdAt }

            SortMode.PRIORITY -> tasks.sortedBy { priorityOrder(it.priority) }

            SortMode.STATUS -> tasks.sortedBy { statusOrder(it.status) }
        }
    }

    private fun priorityOrder(priority: Priority): Int = when (priority) {
        Priority.LOW -> 2
        Priority.MEDIUM -> 1
        Priority.HIGH -> 0
    }

    private fun statusOrder(status: TaskStatus): Int = when (status) {
        TaskStatus.TODO -> 0
        TaskStatus.IN_PROGRESS -> 1
        TaskStatus.DONE -> 2
    }
}