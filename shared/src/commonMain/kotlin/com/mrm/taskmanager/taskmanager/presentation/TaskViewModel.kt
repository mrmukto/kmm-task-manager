package com.mrm.taskmanager.taskmanager.presentation

import com.mrm.taskmanager.taskmanager.data.domain.model.FilterOptions
import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.data.domain.usecase.CreateTaskUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.DeleteTaskUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.FilterTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.GetAllTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.GetTaskByIdUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SearchTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortMode
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortTasksUseCase
import com.mrm.taskmanager.taskmanager.data.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class TaskViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val filterTasksUseCase: FilterTasksUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val sortTasksUseCase: SortTasksUseCase
) {

    // KMM-friendly "viewModelScope"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state

    // Full list from DB – we keep it separately for search/filter/sort
    private var allTasks: List<Task> = emptyList()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        scope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            getAllTasksUseCase().collect { tasks ->
                allTasks = tasks
                _state.update { it.copy(isLoading = false) }
                applyFiltersAndSort()
            }
        }
    }

    // ---------- public API for UI ----------

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFiltersAndSort()
    }

    fun onFilterChange(options: FilterOptions) {
        _state.update { it.copy(filterOptions = options) }
        applyFiltersAndSort()
    }

    fun onSortModeChange(sortMode: SortMode) {
        _state.update { it.copy(sortMode = sortMode) }
        applyFiltersAndSort()
    }

    fun createTask(
        title: String,
        description: String? = null,
        priority: Priority = Priority.MEDIUM,
        status: TaskStatus = TaskStatus.TODO,
        dueDate: Instant? = null
    ) {
        scope.launch {
            try {
                createTaskUseCase(
                    title = title,
                    description = description,
                    priority = priority,
                    status = status,
                    dueDate = dueDate
                )
                // Flow from DB will update list automatically
            } catch (e: Throwable) {
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun updateTask(task: Task) {
        scope.launch {
            try {
                updateTaskUseCase(task)
            } catch (e: Throwable) {
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteTask(id: Long) {
        scope.launch {
            try {
                deleteTaskUseCase(id)
            } catch (e: Throwable) {
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun loadTask(id: Long, onLoaded: (Task?) -> Unit) {
        scope.launch {
            val task = getTaskByIdUseCase(id)
            onLoaded(task)
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun dispose() {
        // Call from platform-specific wrapper when ViewModel is cleared
        scope.coroutineContext.cancel()
    }

    // ---------- internal helpers ----------

    private fun applyFiltersAndSort() {
        val current = _state.value

        val filtered = filterTasksUseCase(allTasks, current.filterOptions)
        val searched = searchTasksUseCase(filtered, current.searchQuery)
        val sorted = sortTasksUseCase(searched, current.sortMode)

        _state.value = current.copy(
            tasks = allTasks,
            visibleTasks = sorted
        )
    }
}