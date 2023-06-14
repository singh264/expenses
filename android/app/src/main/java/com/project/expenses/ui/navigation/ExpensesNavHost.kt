package com.project.expenses.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.expenses.ui.expenses.ExpenseDetailsScreen
import com.project.expenses.ui.expenses.ExpenseEntryScreen
import com.project.expenses.ui.expenses.ExpensesScreen
import com.project.expenses.ui.results.ResultsScreen

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
            ExpensesScreen(
                navigateToExpenseEntry = { navController.navigate(ExpenseEntry.route) },
                navigateToExpenseUpdate = {
                    navController.navigate("${ExpenseDetails.route}/${it}")
                }
            )
        }
        composable(route = Results.route) {
            ResultsScreen()
        }
        composable(route = ExpenseEntry.route) {
            ExpenseEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ExpenseDetails.routeWithArgs,
            arguments = listOf(navArgument(ExpenseDetails.expenseIdArg) {
                type = NavType.IntType
            })
        ) {
            ExpenseDetailsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}