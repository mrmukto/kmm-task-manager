package com.mrm.taskmanager.taskmanager.data.domain.repository

import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    /**
     * Observe all tasks as a flow.
     * Any insert/update/delete should emit a new list.
     */
    fun observeTasks(): Flow<List<Task>>

    /**
     * Get a single task by id, or null if not found.
     */
    suspend fun getTaskById(id: Long): Task?

    /**
     * Create a new task.
     * The Task.id will usually be null; DB will generate it.
     */
    suspend fun createTask(task: Task)

    /**
     * Update an existing task (id must not be null).
     */
    suspend fun updateTask(task: Task)

    /**
     * Delete task by id.
     */
    suspend fun deleteTask(id: Long)
}