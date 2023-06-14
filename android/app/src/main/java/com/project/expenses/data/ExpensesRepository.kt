package com.project.expenses.data

interface ExpensesRepository {
    fun getAllExpenses(): List<Expense>
    fun getExpense(id: Int): Expense?
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
}
