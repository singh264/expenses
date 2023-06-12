package com.example.expenses.ui.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        topBar = { ExpensesScreenTopBar() },
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
            ExpenseBody(
                expenses = viewModel.getExpenses(),
                modifier = modifier
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreenTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Expenses",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}

@Composable
fun ExpenseBody(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        if (expenses.isEmpty()) {
            Text(text = "No expenses found")
        }
        else {
            ExpenseListHeader(modifier)
            LazyColumn {
                items(expenses) { expense ->
                    Row {
                        Text(text = "${expense.date}", modifier = modifier.weight(1.0f))
                        Text(text = "${expense.description}", modifier = modifier.weight(1.5f))
                        Text(text = "${expense.kind}", modifier = modifier.weight(1.0f))
                        Text(
                            text =
                                if (expense.isIncome) {
                                    "$${expense.price}"
                                }
                                else {
                                    "-$${expense.price}"
                                },
                            color = if (expense.isIncome) Color.Green else Color.Red,
                            modifier = modifier.weight(1.0f))
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ExpenseListHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        headerList.forEach { header ->
            Text(
                text = header.label,
                modifier = modifier.weight(header.weight),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseScreenTopBarPreview() {
    ExpensesTheme {
        ExpensesScreenTopBar()
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListHeaderPreview() {
    ExpensesTheme {
        ExpenseListHeader()
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

private data class ExpensesHeader(val label: String, val weight: Float)

private val headerList = listOf(
    ExpensesHeader(label = "Date", weight = 1.0f),
    ExpensesHeader(label = "Description", weight = 1.5f),
    ExpensesHeader(label = "Kind", weight = 1.0f),
    ExpensesHeader(label = "Price", weight = 1.0f)
)

@Preview(showBackground = true)
@Composable
fun ExpenseBodyWithoutExpensesPreview() {
    ExpensesTheme {
        ExpenseBody(
            expenses = listOf()
        )
    }
}
