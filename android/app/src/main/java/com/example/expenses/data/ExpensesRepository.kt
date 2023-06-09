package com.example.expenses.data

import kotlinx.coroutines.flow.Flow

interface ExpensesRepository {
    fun getAllExpensesStream(): List<Expense>
    fun getExpenseStream(id: Int): Flow<Expense?>
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
}
