package com.example.expenses.ui.results

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.R
import com.example.expenses.data.Expense
import com.example.expenses.ui.AppViewModelProvider
import com.example.expenses.ui.expenses.ExpenseEntryViewModel
import com.example.expenses.ui.theme.ExpensesTheme
import com.example.expenses.ui.theme.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import java.lang.Math.abs
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    ExpensesTheme {
        Scaffold(
            topBar = { ResultsScreenTopBar() }
        ) { padding ->
            Column(
                modifier = modifier
                    .padding(padding)
                    .semantics { contentDescription = "Results Screen" }
            ) {
                ResultsBody(
                    expenses = viewModel.getExpenses(),
                    modifier = modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreenTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Results",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    )
}

@Composable
fun ResultsBody(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        ) {
        if (expenses.isEmpty()) {
            Text(
                text = "No expenses found",
                modifier = modifier.align(Alignment.Start)
            )
        }
        else {
            PieChart(
                expenses = expenses,
                modifier = modifier
            )
            ResultsHeader(modifier)
            LazyColumn {
                items(getExpensesSummaryData(expenses)) { data ->
                    Row {
                        Text(text = "${data.expenseKind}", modifier = modifier.weight(1.5f))
                        val isDataValueNegative = data.value < 0
                        Text(
                            text =
                                if (isDataValueNegative) {
                                    "-$${abs(data.value)}"
                                } else {
                                    "$${data.value}"
                                },
                            color = if (isDataValueNegative) Color.Red else Color.Green,
                            modifier = modifier.weight(1.0f)
                        )
                    }
                    Divider()
                }
            }
            Spacer(modifier = modifier.padding(16.dp))
            val netIncome = getNetIncome(expenses)
            val isNetIncomeNegative = netIncome < 0
            Text(
                text =
                    if (isNetIncomeNegative) {
                        "The net income is -$${abs(getNetIncome(expenses))}"
                    } else {
                        "The net income is $${getNetIncome(expenses)}"
                    },
                modifier = modifier.align(Alignment.Start)
            )
        }
    }
}

@Composable
fun ResultsHeader(modifier: Modifier = Modifier) {
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

@Composable
fun PieChart(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.size(320.dp)) {
        Crossfade(targetState = expenses) { pieChartData ->
            AndroidView(factory = { context ->
                PieChart(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    this.description.isEnabled = false
                    this.isDrawHoleEnabled = false
                    this.legend.isEnabled = false
                    this.legend.textSize = 14F
                    this.legend.horizontalAlignment =
                        Legend.LegendHorizontalAlignment.CENTER
                    this.setEntryLabelColor(resources.getColor(R.color.white))
                }
            },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp),
                update = {
                    updatePieChartWithData(it, pieChartData)
                }
            )
        }
    }
}

private data class ResultsHeader(val label: String, val weight: Float)

private val headerList = listOf(
    ResultsHeader(label = "Expense Kind", weight = 1.5f),
    ResultsHeader(label = "Net Amount", weight = 1.0f),
)

@Preview(showBackground = true)
@Composable
fun ResultsScreenTopBarPreview() {
    ExpensesTheme {
        ResultsScreenTopBar()
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenBodyWithExpensesPreview() {
    ExpensesTheme {
        ResultsBody(
            expenses = listOf(
                Expense(
                    id = 1,
                    date = "2023/01/01",
                    description = "description",
                    kind = "Food",
                    price = 10.0,
                    isIncome = false
                ),
                Expense(
                    id = 2,
                    date = "2023/01/01",
                    description = "description",
                    kind = "Living accommodation",
                    price = 25.0,
                    isIncome = false
                ),
                Expense(
                    id = 3,
                    date = "2023/01/01",
                    description = "Job",
                    kind = "Job",
                    price = 15.0,
                    isIncome = true
                ),
                Expense(
                    id = 3,
                    date = "2023/01/05",
                    description = "Job",
                    kind = "Job",
                    price = 25.0,
                    isIncome = true
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenBodyWithoutExpensesPreview() {
    ExpensesTheme {
        ResultsBody(
            expenses = listOf()
        )
    }
}