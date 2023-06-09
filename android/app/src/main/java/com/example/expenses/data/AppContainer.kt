package com.example.expenses.data

import android.content.Context

interface AppContainer {
    val expensesRepository: ExpensesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val expensesRepository: ExpensesRepository by lazy {
        OfflineExpensesRepository(ExpensesDatabase.getDatabase(context).expenseDao())
    }
}
