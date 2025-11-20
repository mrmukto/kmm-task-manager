package com.mrm.taskmanager.taskmanager.data.domain.model

data class FilterOptions(
    val status: TaskStatus? = null,
    val priority: Priority? = null,
)
