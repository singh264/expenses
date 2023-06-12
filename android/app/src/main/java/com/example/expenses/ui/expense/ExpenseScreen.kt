package com.example.expenses.ui.expense

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.data.Expense
import com.example.expenses.ui.AppViewModelProvider
import com.example.expenses.ui.theme.ExpensesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navigateToExpenseEntry: () -> Unit,
    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Expenses") }, modifier = modifier)
         },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToExpenseEntry,
                modifier = modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .semantics { contentDescription = "Expenses Screen" }
        ) {
            ExpenseBody(expenses = viewModel.getExpenses())
        }
    }
//    Column(
//        modifier = modifier.semantics { contentDescription = "Expenses Screen" }
//    ) {
//        ExpenseBody(expenses = viewModel.getExpenses())
//    }
}

@Composable
fun ExpenseBody(expenses: List<Expense>) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (expenses.isEmpty()) {
            Text(text = "No expenses found")
        }
        else {
            LazyColumn {
                items(expenses) { expense ->
                    Text(text = "Expense date: ${expense.date}")
                    Text(text = "Expense description: ${expense.description}")
                    Text(text = "Expense kind: ${expense.kind}")
                    Text(text = "Expense price: ${expense.price}")
                    Text(text = "Expense isIncome: ${expense.isIncome}")
                    Text(text = "")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseBodyWithExpensesPreview() {
    ExpensesTheme {
        ExpenseBody(
            expenses = listOf(
                Expense(
                    id=1,
                    date="2023/01/01",
                    description="description",
                    kind="Food",
                    price=1.0,
                    isIncome=false),
                Expense(
                    id=2,
                    date="2023/01/01",
                    description="description",
                    kind="Living accommodation",
                    price=25.0,
                    isIncome=false),
                Expense(
                    id=3,
                    date="2023/01/01",
                    description="Job",
                    kind="Job",
                    price=15.0,
                    isIncome=true),
                Expense(
                    id=3,
                    date="2023/01/05",
                    description="Job",
                    kind="Job",
                    price=25.0,
                    isIncome=true)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseBodyWithoutExpensesPreview() {
    ExpensesTheme {
        ExpenseBody(
            expenses = listOf()
        )
    }
}
