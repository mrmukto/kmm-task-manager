package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepository

class GetTaskByIdUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long): Task? {
        return repository.getTaskById(id)
    }
}