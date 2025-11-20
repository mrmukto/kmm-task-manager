package com.mrm.taskmanager.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrm.taskmanager.taskmanager.ui.TaskApp
import com.mrm.taskmanager.taskmanager.ui.theme.TaskManagerTheme
import com.mrm.taskmanager.taskmanager.viewmodel.AndroidTaskViewModel
import com.mrm.taskmanager.taskmanager.viewmodel.AndroidTaskViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskManagerTheme {
                val context = LocalContext.current
                val vm: AndroidTaskViewModel = viewModel(
                    factory = AndroidTaskViewModelFactory(context.applicationContext)
                )
                val state by vm.state.collectAsState()

                TaskApp(
                    uiState = state,
                    viewModel = vm
                )
            }
        }
    }
}
