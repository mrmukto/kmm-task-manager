package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.Task

class SearchTasksUseCase {

    operator fun invoke(
        tasks: List<Task>,
        query: String
    ): List<Task> {
        if (query.isBlank()) return tasks
        val lower = query.lowercase()
        return tasks.filter { task ->
            task.title.lowercase().contains(lower)
        }
    }
}