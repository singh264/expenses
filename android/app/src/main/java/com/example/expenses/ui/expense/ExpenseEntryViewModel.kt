package com.example.expenses.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.expenses.data.Expense
import com.example.expenses.data.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ExpenseEntryViewModel(private val expensesRepository: ExpensesRepository) : ViewModel() {
    //    var expenseUiState by rememberSaveable { mutableStateOf(ExpenseUiState()) }
    var expenseUiState by mutableStateOf(ExpenseUiState())
        private set

    fun updateUiState(newExpenseUiState: ExpenseUiState) {
        expenseUiState = newExpenseUiState.copy(actionEnabled = newExpenseUiState.isValid())
    }

    fun resetUiState() {
        expenseUiState = ExpenseUiState()
    }

    fun getExpenses(): List<Expense> {
        return expensesRepository.getAllExpensesStream()
    }

    suspend fun saveExpense() {
        if (expenseUiState.isValid()) {
            expensesRepository.insertExpense(expenseUiState.toExpense())
        }
    }
}
