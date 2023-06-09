package com.example.expenses.data

import kotlinx.coroutines.flow.Flow

class OfflineExpensesRepository(private val expenseDao: ExpenseDao) : ExpensesRepository {
    override fun getAllExpensesStream(): List<Expense> = expenseDao.getAllExpenses()
    override fun getExpenseStream(id: Int): Flow<Expense?> = expenseDao.getExpense(id)
    override suspend fun insertExpense(expense: Expense) = expenseDao.insert(expense)
    override suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)
    override suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)
}
