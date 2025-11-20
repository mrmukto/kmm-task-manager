package com.mrm.taskmanager.taskmanager.presentation

import com.mrm.taskmanager.taskmanager.data.domain.model.FilterOptions
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortMode

data class TaskUiState(
    val tasks: List<Task> = emptyList(),          // all tasks from DB
    val visibleTasks: List<Task> = emptyList(),   // after search/filter/sort

    val searchQuery: String = "",
    val filterOptions: FilterOptions = FilterOptions(),
    val sortMode: SortMode = SortMode.CREATED_AT_DESC,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
