package com.mrm.taskmanager.taskmanager.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mrm.taskmanager.taskmanager.data.domain.model.FilterOptions
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.data.domain.usecase.SortMode
import com.mrm.taskmanager.taskmanager.presentation.TaskUiState
import com.mrm.taskmanager.taskmanager.ui.components.SearchBar
import com.mrm.taskmanager.taskmanager.ui.components.TaskItem

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    state: TaskUiState,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (FilterOptions) -> Unit,
    onSortModeChange: (SortMode) -> Unit,
    onTaskClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    onDeleteTask: (Long) -> Unit
) {
    Scaffold(
        topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Task Manager") }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )


    }, floatingActionButton = {
        FloatingActionButton(
            onClick = onAddTaskClick, containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add task")
        }
    }, containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp)
        ) {
            // 🔹 Top card: search + filter/sort
            Surface(
                shape = MaterialTheme.shapes.large, tonalElevation = 3.dp, modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SearchBar(
                        query = state.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        modifier = Modifier.fillMaxWidth()
                    )

                    FilterSortRow(
                        currentFilter = state.filterOptions,
                        currentSortMode = state.sortMode,
                        onFilterChange = onFilterChange,
                        onSortModeChange = onSortModeChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (state.visibleTasks.isNotEmpty()) {
                Text(
                    text = "Your tasks",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (state.visibleTasks.isEmpty() && !state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No tasks yet. Tap + to add one.", color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.visibleTasks) { task ->
                        TaskItem(
                            task = task,
                            onClick = { task.id?.let(onTaskClick) },
                            onDeleteClick = { task.id?.let(onDeleteTask) })
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSortRow(
    currentFilter: FilterOptions,
    currentSortMode: SortMode,
    onFilterChange: (FilterOptions) -> Unit,
    onSortModeChange: (SortMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        StatusFilter(
            currentFilter = currentFilter, onFilterChange = onFilterChange, modifier = Modifier.weight(1f)
        )

        SortDropdown(
            currentSortMode = currentSortMode, onSortModeChange = onSortModeChange, modifier = Modifier.weight(1f)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortDropdown(
    currentSortMode: SortMode, onSortModeChange: (SortMode) -> Unit, modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        val label = when (currentSortMode) {
            SortMode.CREATED_AT_DESC -> "Newest"
            SortMode.CREATED_AT_ASC -> "Oldest"
            SortMode.PRIORITY -> "Priority"
            SortMode.STATUS -> "Status"
        }

        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Sort") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Newest first") }, onClick = {
                onSortModeChange(SortMode.CREATED_AT_DESC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Oldest first") }, onClick = {
                onSortModeChange(SortMode.CREATED_AT_ASC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Priority") }, onClick = {
                onSortModeChange(SortMode.PRIORITY)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Status") }, onClick = {
                onSortModeChange(SortMode.STATUS)
                expanded = false
            })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusFilter(
    currentFilter: FilterOptions, onFilterChange: (FilterOptions) -> Unit, modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        val label = when (currentFilter.status) {
            null -> "All"
            TaskStatus.TODO -> "TODO"
            TaskStatus.IN_PROGRESS -> "In Progress"
            TaskStatus.DONE -> "Done"
        }

        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Status") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("All") }, onClick = {
                onFilterChange(
                    FilterOptions(
                        status = null, priority = currentFilter.priority
                    )
                )
                expanded = false
            })
            DropdownMenuItem(text = { Text("TODO") }, onClick = {
                onFilterChange(currentFilter.copy(status = TaskStatus.TODO))
                expanded = false
            })
            DropdownMenuItem(text = { Text("In Progress") }, onClick = {
                onFilterChange(currentFilter.copy(status = TaskStatus.IN_PROGRESS))
                expanded = false
            })
            DropdownMenuItem(text = { Text("Done") }, onClick = {
                onFilterChange(currentFilter.copy(status = TaskStatus.DONE))
                expanded = false
            })
        }
    }
}



