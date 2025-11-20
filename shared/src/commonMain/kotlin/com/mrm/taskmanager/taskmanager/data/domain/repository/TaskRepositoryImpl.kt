package com.mrm.taskmanager.taskmanager.data.domain.repository



import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.kmm.taskmanager.database.TaskDatabase
import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.data.model.TaskEntity
import comkmmtaskmanager.database.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class TaskRepositoryImpl(
    private val db: TaskDatabase
) : TaskRepository {

    // ✅ Use the actual generated queries property:
    // public val taskDatabaseQueries: TaskDatabaseQueries
    private val queries = db.taskDatabaseQueries

    override fun observeTasks(): Flow<List<Task>> {
        return queries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return queries
            .selectById(id)
            .executeAsOneOrNull()
            ?.toDomain()
    }

    override suspend fun createTask(task: Task) {
        queries.insertTask(
            title = task.title,
            description = task.description,
            priority = task.priority.name,
            status = task.status.name,
            dueDate = task.dueDate?.toString(),
            createdAt = task.createdAt.toString()
        )
    }

    override suspend fun updateTask(task: Task) {
        val id = task.id ?: return  // or throw IllegalArgumentException

        queries.updateTask(
            title = task.title,
            description = task.description,
            priority = task.priority.name,
            status = task.status.name,
            dueDate = task.dueDate?.toString(),
            id = id
        )
    }

    override suspend fun deleteTask(id: Long) {
        queries.deleteTask(id)
    }

    // ---------- Mapping helpers ----------

    private fun Tasks.toEntity(): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            description = description,
            priority = Priority.valueOf(priority),
            status = TaskStatus.valueOf(status),
            dueDate = dueDate?.let { Instant.parse(it) },
            createdAt = Instant.parse(createdAt)
        )
    }

    private fun Tasks.toDomain(): Task {
        return toEntity().toDomain()
    }

    private fun TaskEntity.toDomain(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            priority = priority,
            status = status,
            dueDate = dueDate,
            createdAt = createdAt
        )
    }
}