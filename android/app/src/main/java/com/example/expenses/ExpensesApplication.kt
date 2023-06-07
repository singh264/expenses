package com.example.expenses

import android.app.Application
import com.example.expenses.data.AppContainer
import com.example.expenses.data.AppDataContainer

class ExpensesApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}