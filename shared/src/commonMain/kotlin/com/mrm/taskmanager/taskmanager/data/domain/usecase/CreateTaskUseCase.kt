package com.mrm.taskmanager.taskmanager.data.domain.usecase

import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class CreateTaskUseCase(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(
        title: String,
        description: String? = null,
        priority: Priority = Priority.MEDIUM,
        status: TaskStatus = TaskStatus.TODO,
        dueDate: Instant? = null
    ) {
        val now = Clock.System.now()
        val newTask = Task(
            id = null,
            title = title,
            description = description,
            priority = priority,
            status = status,
            dueDate = dueDate,
            createdAt = now
        )
        repository.createTask(newTask)
    }
}