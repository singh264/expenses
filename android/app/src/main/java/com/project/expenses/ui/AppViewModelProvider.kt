package com.project.expenses.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.project.expenses.ExpensesApplication
import com.project.expenses.ui.expenses.ExpensesViewModel

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