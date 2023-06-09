package com.example.expenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.expenses.ui.expense.ExpenseScreen
import com.example.expenses.ui.results.ResultsScreen
import com.example.expenses.ui.theme.ExpensesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpensesTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var expensesEnabled by rememberSaveable { mutableStateOf(true) }
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    onExpensesClicked = { expensesEnabled =  true },
                    onResultsClicked = { expensesEnabled =  false }
                ) }
        ) { padding ->
            if (expensesEnabled) {
                Column(modifier = modifier.padding(padding)) {
                    ExpenseScreen()
                }
            }
            else {
                Column(modifier = modifier.padding(padding)) {
                    ResultsScreen()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onExpensesClicked: () -> Unit,
    onResultsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
    ) {
        NavigationBarItem(
            selected = true,
            onClick = onExpensesClicked,
            icon = { Icon(Icons.Default.Home, contentDescription = null)},
            label = { Text(text = "Expense")}
        )
        NavigationBarItem(
            selected = true,
            onClick = onResultsClicked,
            icon = { Icon(Icons.Default.Menu, contentDescription = null)},
            label = { Text(text = "Results")}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    var expensesEnabled by rememberSaveable { mutableStateOf(false) }
    ExpensesTheme {
        BottomNavigationBar(
            onExpensesClicked = { expensesEnabled = true },
            onResultsClicked = { expensesEnabled = false }
        )
    }
}
