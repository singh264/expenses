package com.example.expenses.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expenses.ui.expense.ExpensesScreen
import com.example.expenses.ui.results.ResultsScreen

@Composable
fun ExpensesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Expenses.route,
        modifier = modifier
    ) {
        composable(route = Expenses.route) {
            ExpensesScreen()
        }
        composable(route = Results.route) {
            ResultsScreen()
        }
    }
}