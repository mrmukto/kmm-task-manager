package com.mrm.taskmanager.taskmanager.ui.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mrm.taskmanager.taskmanager.presentation.TaskUiState
import com.mrm.taskmanager.taskmanager.ui.screens.AddEditTaskScreen
import com.mrm.taskmanager.taskmanager.ui.screens.TaskListScreen
import com.mrm.taskmanager.taskmanager.viewmodel.AndroidTaskViewModel

object Routes {
    const val TASK_LIST = "task_list"
    const val ADD_TASK = "add_task"
    const val EDIT_TASK = "edit_task"
    const val ARG_TASK_ID = "taskId"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskNavHost(
    navController: NavHostController,
    uiState: TaskUiState,
    viewModel: AndroidTaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TASK_LIST
    ) {
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                state = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onFilterChange = viewModel::onFilterChange,
                onSortModeChange = viewModel::onSortModeChange,
                onTaskClick = { id ->
                    navController.navigate("${Routes.EDIT_TASK}/$id")
                },
                onAddTaskClick = {
                    navController.navigate(Routes.ADD_TASK)
                },
                onDeleteTask = viewModel::deleteTask
            )
        }

        composable(Routes.ADD_TASK) {
            AddEditTaskScreen(
                taskId = null,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = "${Routes.EDIT_TASK}/{${Routes.ARG_TASK_ID}}",
            arguments = listOf(
                navArgument(Routes.ARG_TASK_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Routes.ARG_TASK_ID)
            AddEditTaskScreen(
                taskId = id,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
