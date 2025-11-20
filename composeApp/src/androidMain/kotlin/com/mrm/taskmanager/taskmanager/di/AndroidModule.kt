package com.mrm.taskmanager.taskmanager.di

import android.content.Context
import com.mrm.taskmanager.taskmanager.presentation.TaskViewModel
import com.mrm.taskmanager.taskmanager.presentation.di.SharedModule

object AndroidModule {

    fun provideTaskViewModel(context: Context): TaskViewModel {
        val driverFactory = PlatformModule.provideDatabaseDriverFactory(context)
        return SharedModule.provideTaskViewModel(driverFactory)
    }
}