package com.mrm.taskmanager.taskmanager.presentation

import com.mrm.taskmanager.taskmanager.data.domain.model.FilterOptions
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortMode

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val visibleTasks: List<Task> = emptyList(),

    val searchQuery: String = "",
    val filterOptions: FilterOptions = FilterOptions(),
    val sortMode: SortMode = SortMode.CREATED_AT_DESC,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
