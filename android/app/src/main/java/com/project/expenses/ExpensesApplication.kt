package com.project.expenses

import android.app.Application
import com.project.expenses.data.AppContainer
import com.project.expenses.data.AppDataContainer

class ExpensesApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}