package com.mrm.taskmanager.taskmanager.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.kmm.taskmanager.database.TaskDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = TaskDatabase.Schema,
            context = context,
            name = "task_manager.db"
        )
    }
}