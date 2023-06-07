package com.example.expenses.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expenses.ExpensesApplication
import com.example.expenses.ui.expense.ExpenseEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExpenseEntryViewModel(expensesApplication().container.expensesRepository)
        }
    }
}

fun CreationExtras.expensesApplication(): ExpensesApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExpensesApplication)