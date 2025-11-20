package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.FilterOptions
import com.mrm.taskmanager.taskmanager.data.domain.model.Task

class FilterTasksUseCase {

    operator fun invoke(
        tasks: List<Task>,
        options: FilterOptions
    ): List<Task> {
        return tasks.filter { task ->
            val statusMatches = options.status?.let { it == task.status } ?: true
            val priorityMatches = options.priority?.let { it == task.priority } ?: true
            statusMatches && priorityMatches
        }
    }
}