package com.example.expenses.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

interface ExpensesDestination {
    val route: String
    val label: String
    val icon: @Composable () -> Unit
}

object Expenses : ExpensesDestination {
    override val route = "expenses"
    override val label = "Expenses"
    override val icon: @Composable () -> Unit = {
        Icon(Icons.Default.Home, contentDescription = null)
    }
}

object Results : ExpensesDestination {
    override val route = "results"
    override val label = "Results"
    override val icon: @Composable () -> Unit = {
        Icon(Icons.Default.Menu, contentDescription = null)
    }
}

object ExpenseEntry : ExpensesDestination {
    override val route = "expense_entry"
    override val label = ""
    override val icon: @Composable () -> Unit = {}
}

val bottomNavigationBarScreens = listOf(Expenses, Results)