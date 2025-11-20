package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.observeTasks()
    }
}