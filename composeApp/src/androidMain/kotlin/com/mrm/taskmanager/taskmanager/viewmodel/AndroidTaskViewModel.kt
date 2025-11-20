package com.mrm.taskmanager.taskmanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mrm.taskmanager.taskmanager.data.domain.model.FilterOptions
import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortMode
import com.mrm.taskmanager.taskmanager.di.AndroidModule
import com.mrm.taskmanager.taskmanager.presentation.TaskUiState
import com.mrm.taskmanager.taskmanager.presentation.TaskViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

class AndroidTaskViewModel(
    private val sharedViewModel: TaskViewModel
) : ViewModel() {

    val state: StateFlow<TaskUiState> = sharedViewModel.state

    fun onSearchQueryChange(query: String) = sharedViewModel.onSearchQueryChange(query)

    fun onFilterChange(options: FilterOptions) = sharedViewModel.onFilterChange(options)

    fun onSortModeChange(sortMode: SortMode) = sharedViewModel.onSortModeChange(sortMode)

    fun createTask(
        title: String,
        description: String? = null,
        priority: Priority = Priority.MEDIUM,
        status: TaskStatus = TaskStatus.TODO,
        dueDate: Instant? = null
    ) = sharedViewModel.createTask(
        title = title, description = description, priority = priority, status = status, dueDate = dueDate
    )

    fun updateTask(task: Task) = sharedViewModel.updateTask(task)

    fun deleteTask(id: Long) = sharedViewModel.deleteTask(id)

    fun loadTask(id: Long, onLoaded: (Task?) -> Unit) = sharedViewModel.loadTask(id, onLoaded)

    fun clearError() = sharedViewModel.clearError()

    override fun onCleared() {
        super.onCleared()
        sharedViewModel.dispose()
    }
}

class AndroidTaskViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val sharedVm = AndroidModule.provideTaskViewModel(appContext)
        return AndroidTaskViewModel(sharedVm) as T
    }
}