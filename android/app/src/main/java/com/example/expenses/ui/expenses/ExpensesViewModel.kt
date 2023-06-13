package com.example.expenses.ui.expenses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.expenses.data.Expense
import com.example.expenses.data.ExpensesRepository
import com.example.expenses.ui.navigation.ExpenseDetails

class ExpensesViewModel(
    private val expensesRepository: ExpensesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val expenseId: Int? = savedStateHandle[ExpenseDetails.expenseIdArg]

    var expenseUiState by mutableStateOf(ExpenseUiState())
        private set

    fun updateUiState(newExpenseUiState: ExpenseUiState) {
        expenseUiState = newExpenseUiState.copy(actionEnabled = newExpenseUiState.isValid())
    }

    fun resetUiState() {
        expenseUiState = ExpenseUiState()
    }

    fun getExpenses(): List<Expense> {
        return expensesRepository.getAllExpenses()
    }

    suspend fun saveExpense() {
        if (expenseUiState.isValid()) {
            expensesRepository.insertExpense(expenseUiState.toExpense())
        }
    }

    suspend fun updateExpense() {
        if (expenseUiState.isValid()) {
            expensesRepository.updateExpense(expenseUiState.toExpense())
        }
    }

    suspend fun deleteExpense() {
        if (expenseUiState.isValid()) {
            expensesRepository.deleteExpense(expenseUiState.toExpense())
        }
    }

    fun loadExpense() {
        val id = expenseId;
        if (id != null) {
            val expense = expensesRepository.getExpense(id);
            if (expense != null) {
                updateUiState(expenseUiState.copy(id = expense.id))
                updateUiState(expenseUiState.copy(date = expense.date))
                updateUiState(expenseUiState.copy(description = expense.description))
                updateUiState(expenseUiState.copy(kind = expense.kind))
                updateUiState(expenseUiState.copy(price = expense.price.toString()))
                updateUiState(expenseUiState.copy(isIncome = expense.isIncome))
            }
        }
    }
}
