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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    ExpensesTheme {
        Column(
            modifier = modifier.semantics { contentDescription = "Results Screen" }
        ) {
            Surface(color = MaterialTheme.colorScheme.primary) {
                Scaffold(
                    topBar = { ResultsScreenTopBar() }
                ) { padding ->
                    ResultsScreenBody(
                        expenses = viewModel.getExpenses(),
                        modifier = Modifier.padding(padding)
                    )
                }
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
fun ResultsScreenBody(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Expenses",
                style = TextStyle.Default,
                fontFamily = FontFamily.Default,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp
            )

            PieChart(
                expenses = expenses,
                modifier = modifier
            )

            LazyColumn {
                items(getExpensesSummaryData(expenses)) { data ->
                    Text(text = "${data.expenseKind}: $${data.value}")
                }
            }

            Text(text = "")
            Text(text = "The net income is $${getNetIncome(expenses)}")
        }
    }
}

@Composable
fun PieChart(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(18.dp)
            .size(320.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Crossfade(targetState = expenses) { pieChartData ->
            AndroidView(factory = { context ->
                PieChart(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    this.description.isEnabled = false
                    this.isDrawHoleEnabled = false
                    this.legend.isEnabled = true
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

@Preview(showBackground = true)
@Composable
fun ResultsScreenTopBarPreview() {
    ExpensesTheme {
        ResultsScreenTopBar()
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenBodyPreview() {
    ExpensesTheme {
        ResultsScreenBody(
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