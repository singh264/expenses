package com.project.expenses.data

class OfflineExpensesRepository(private val expenseDao: ExpenseDao) : ExpensesRepository {
    override fun getAllExpenses(): List<Expense> = expenseDao.getAllExpenses()
    override fun getExpense(id: Int): Expense? = expenseDao.getExpense(id)
    override suspend fun insertExpense(expense: Expense) = expenseDao.insert(expense)
    override suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)
    override suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)
}
