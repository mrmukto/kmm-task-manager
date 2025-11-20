package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepository

class UpdateTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        requireNotNull(task.id) { "Cannot update a task without an id" }
        repository.updateTask(task)
    }
}