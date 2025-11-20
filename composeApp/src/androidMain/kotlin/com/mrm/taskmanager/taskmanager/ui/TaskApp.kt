package com.mrm.taskmanager.taskmanager.ui

import androidx.compose.material3.MaterialTheme
import com.mrm.taskmanager.taskmanager.presentation.TaskUiState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mrm.taskmanager.taskmanager.ui.navigation.TaskNavHost
import com.mrm.taskmanager.taskmanager.ui.theme.TaskManagerTheme
import com.mrm.taskmanager.taskmanager.viewmodel.AndroidTaskViewModel

@Composable
fun TaskApp(
    uiState: TaskUiState,
    viewModel: AndroidTaskViewModel
) {
    TaskManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()

            TaskNavHost(
                navController = navController,
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}