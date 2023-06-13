package com.example.expenses.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expenses.ExpensesApplication
import com.example.expenses.ui.expenses.ExpensesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExpensesViewModel(
                expensesApplication().container.expensesRepository,
                this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.expensesApplication(): ExpensesApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExpensesApplication)