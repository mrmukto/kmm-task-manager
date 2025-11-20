package com.mrm.taskmanager.taskmanager.presentation.di

import com.kmm.taskmanager.database.TaskDatabase
import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepository
import com.mrm.taskmanager.taskmanager.data.domain.repository.TaskRepositoryImpl
import com.mrm.taskmanager.taskmanager.data.domain.usecase.CreateTaskUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.DeleteTaskUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.FilterTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.GetAllTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.GetTaskByIdUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SearchTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.UpdateTaskUseCase
import com.mrm.taskmanager.taskmanager.data.local.DatabaseDriverFactory
import com.mrm.taskmanager.taskmanager.presentation.TaskViewModel

object SharedModule {

    fun provideDatabase(driverFactory: DatabaseDriverFactory): TaskDatabase {
        return TaskDatabase(driverFactory.createDriver())
    }

    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db)
    }

    fun provideTaskViewModel(
        driverFactory: DatabaseDriverFactory
    ): TaskViewModel {
        val db = provideDatabase(driverFactory)
        val repo = provideTaskRepository(db)

        val getAllTasksUseCase = GetAllTasksUseCase(repo)
        val getTaskByIdUseCase = GetTaskByIdUseCase(repo)
        val createTaskUseCase = CreateTaskUseCase(repo)
        val updateTaskUseCase = UpdateTaskUseCase(repo)
        val deleteTaskUseCase = DeleteTaskUseCase(repo)
        val filterTasksUseCase = FilterTasksUseCase()
        val searchTasksUseCase = SearchTasksUseCase()
        val sortTasksUseCase = SortTasksUseCase()

        return TaskViewModel(
            getAllTasksUseCase = getAllTasksUseCase,
            getTaskByIdUseCase = getTaskByIdUseCase,
            createTaskUseCase = createTaskUseCase,
            updateTaskUseCase = updateTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            filterTasksUseCase = filterTasksUseCase,
            searchTasksUseCase = searchTasksUseCase,
            sortTasksUseCase = sortTasksUseCase
        )
    }
}
