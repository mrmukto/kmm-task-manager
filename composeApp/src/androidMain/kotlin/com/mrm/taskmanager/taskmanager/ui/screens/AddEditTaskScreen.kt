package com.mrm.taskmanager.taskmanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mrm.taskmanager.taskmanager.data.domain.model.Priority
import com.mrm.taskmanager.taskmanager.data.domain.model.Task
import com.mrm.taskmanager.taskmanager.data.domain.model.TaskStatus
import com.mrm.taskmanager.taskmanager.viewmodel.AndroidTaskViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Long?,
    viewModel: AndroidTaskViewModel,
    navController: NavController
) {
    var isLoaded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var status by remember { mutableStateOf(TaskStatus.TODO) }
    var createdAt by remember { mutableStateOf<Instant?>(null) }

    var dueDate by remember { mutableStateOf<Instant?>(null) }
    var isDatePickerOpen by remember { mutableStateOf(false) }

    var titleError by remember { mutableStateOf<String?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId) { task: Task? ->
                task?.let {
                    title = it.title
                    description = it.description.orEmpty()
                    priority = it.priority
                    status = it.status
                    createdAt = it.createdAt
                    dueDate = it.dueDate   // load existing due date if any
                }
                isLoaded = true
            }
        } else {
            isLoaded = true
        }
    }

    val createdLabel = remember(createdAt) {
        createdAt?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault())
            "Created at: " + formatter.format(it.toJavaInstant())
        } ?: ""
    }

    val dueDateLabel = remember(dueDate) {
        dueDate?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())
            formatter.format(it.toJavaInstant())
        } ?: "No due date"
    }

    val datePickerState = rememberDatePickerState()

    if (isDatePickerOpen) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            dueDate = Instant.fromEpochMilliseconds(millis)
                        }
                        isDatePickerOpen = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerOpen = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") }
            )
        }
    ) { padding ->
        if (!isLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp) // keep global spacing
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            if (titleError != null && it.isNotBlank()) {
                                titleError = null
                            }
                        },
                        label = { Text("Title *") },
                        isError = titleError != null,
                        supportingText = {
                            if (titleError != null) {
                                Text(
                                    text = titleError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        minLines = 3    // compact multi-line, no big empty box

                    )
                }

                var priorityExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = priorityExpanded,
                    onExpandedChange = { priorityExpanded = !priorityExpanded }
                ) {
                    OutlinedTextField(
                        value = priority.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = priorityExpanded,
                        onDismissRequest = { priorityExpanded = false }
                    ) {
                        Priority.values().forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    priority = p
                                    priorityExpanded = false
                                }
                            )
                        }
                    }
                }

                // Status selector
                var statusExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    OutlinedTextField(
                        value = status.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        TaskStatus.values().forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s.name) },
                                onClick = {
                                    status = s
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = dueDateLabel,
                    onValueChange = { /* read-only */ },
                    readOnly = true,
                    label = { Text("Due date (optional)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDatePickerOpen = true },
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(onClick = { isDatePickerOpen = true }) {
                        Text("Pick date")
                    }
                    if (dueDate != null) {
                        TextButton(onClick = { dueDate = null }) {
                            Text("Clear")
                        }
                    }
                }

                // Created timestamp only for existing task
                if (taskId != null && createdLabel.isNotBlank()) {
                    Text(
                        text = createdLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val trimmedTitle = title.trim()
                        if (trimmedTitle.isBlank()) {
                            titleError = "Title is required"
                            return@Button
                        }

                        if (taskId == null) {
                            // Create – createdAt handled in use case / repo
                            viewModel.createTask(
                                title = trimmedTitle,
                                description = description.takeIf { it.isNotBlank() },
                                priority = priority,
                                status = status,
                                dueDate = dueDate
                            )
                            successMessage = "Task added successfully"
                            showSuccessDialog = true
                        } else {
                            // Update – keep original createdAt
                            viewModel.loadTask(taskId) { existing ->
                                if (existing != null) {
                                    val updated = existing.copy(
                                        title = trimmedTitle,
                                        description = description.takeIf { it.isNotBlank() },
                                        priority = priority,
                                        status = status,
                                        dueDate = dueDate
                                    )
                                    viewModel.updateTask(updated)
                                    successMessage = "Task updated successfully"
                                    showSuccessDialog = true
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (taskId == null) "Save Task" else "Update Task")
                }
            }
        }
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                    navController.popBackStack()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("OK")
                    }
                },
                title = { Text("Success") },
                text = { Text(successMessage) }
            )
        }
    }
}
