package com.project.expenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.expenses.ui.navigation.Expenses
import com.project.expenses.ui.navigation.ExpensesDestination
import com.project.expenses.ui.navigation.ExpensesNavHost
import com.project.expenses.ui.navigation.bottomNavigationBarScreens
import com.project.expenses.ui.theme.ExpensesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpensesTheme {
                ExpensesApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesApp(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController: NavHostController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = bottomNavigationBarScreens.find {
            it.route == currentDestination?.route } ?: Expenses
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    allScreens = bottomNavigationBarScreens,
                    onNavigationBarItemClick = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route) },
                    currentScreen = currentScreen
                )
            }
        ) { padding ->
            ExpensesNavHost(
                navController = navController,
                modifier = modifier.padding(padding)
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

@Composable
fun BottomNavigationBar(
    allScreens: List<ExpensesDestination>,
    onNavigationBarItemClick: (ExpensesDestination) -> Unit,
    currentScreen: ExpensesDestination
) {
    NavigationBar() {
        allScreens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onNavigationBarItemClick(screen) },
                label = { Text(text = screen.label) },
                icon = screen.icon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    var currentScreen: ExpensesDestination by remember { mutableStateOf(Expenses) }
    ExpensesTheme {
        BottomNavigationBar(
            allScreens = bottomNavigationBarScreens,
            onNavigationBarItemClick = { screen -> currentScreen = screen },
            currentScreen = currentScreen
        )
    }
}
