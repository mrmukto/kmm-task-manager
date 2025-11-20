package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTask(id)
    }
}