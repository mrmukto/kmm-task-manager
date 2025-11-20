package com.mrm.taskmanager.taskmanager.di

import android.content.Context
import com.mrm.taskmanager.taskmanager.data.local.DatabaseDriverFactory

object PlatformModule {
    fun provideDatabaseDriverFactory(context: Context): DatabaseDriverFactory {
        return DatabaseDriverFactory(context)
    }
}