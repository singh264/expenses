package com.example.expenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.data.Expense
import com.example.expenses.ui.AppViewModelProvider
import com.example.expenses.ui.expense.ExpenseEntryViewModel
import com.example.expenses.ui.expense.ExpenseUiState
import com.example.expenses.ui.theme.ExpensesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

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
                ExpenseBottomNavigationBar(
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
fun ExpenseBottomNavigationBar(
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

@Composable
fun ResultsScreen() {
    Text(text = "Results page")
}

@Composable
fun ExpenseScreen(
    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    var expenseYear by rememberSaveable { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var expenseMonth by rememberSaveable { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var expenseDay by rememberSaveable { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var expenseDate by rememberSaveable { mutableStateOf("") }
    viewModel.updateUiState(viewModel.expenseUiState.copy(date = expenseDate))
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        {_: DatePicker, expenseYear: Int, expenseMonth: Int, expenseDay: Int ->
            expenseDate = "$expenseDay/$expenseMonth/$expenseYear"
        }, expenseYear, expenseMonth, expenseDay
    )

    ExpenseScreenBody(
        expenseDate = expenseDate,
        onExpenseFormSaveButtonClick =  {
            coroutineScope.launch {
                viewModel.saveExpense()
                viewModel.resetUiState()
                expenseDate = ""
            }
        },
        datePickerDialog = datePickerDialog,
        expenseUiState = viewModel.expenseUiState,
        onExpenseValueChange = viewModel::updateUiState,
        expenses = viewModel.getExpenses()
    )
}

@Composable
fun ExpenseScreenBody(
    expenseDate: String,
    onExpenseFormSaveButtonClick: () -> Unit,
    datePickerDialog: DatePickerDialog,
    expenseUiState: ExpenseUiState,
    onExpenseValueChange: (ExpenseUiState) -> Unit,
    expenses: List<Expense>
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpenseFormDate(
            expenseDate = expenseDate,
            onButtonClick = { datePickerDialog.show() },
        )
        ExpenseFormDescription(
            expenseUiState = expenseUiState,
            onExpenseValueChange = onExpenseValueChange
        )
        ExpenseFormKind(
            expenseUiState = expenseUiState,
            onExpenseValueChange = onExpenseValueChange
        )
        ExpenseFormPrice(
            expenseUiState = expenseUiState,
            onExpenseValueChange = onExpenseValueChange
        )
        ExpenseFormSaveButton(
            onClick = onExpenseFormSaveButtonClick,
            expenseUiState = expenseUiState,
        )

        LazyColumn {
            items(expenses) {expense ->
                Text(text = "Expense date: ${expense.date}")
                Text(text = "Expense description: ${expense.description}")
                Text(text = "Expense kind: ${expense.kind}")
                Text(text = "Expense price: ${expense.price}")
                Text(text = "")
            }
        }
    }
}

@Composable
fun ExpenseFormDate(
    expenseDate: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Menu,
            contentDescription = null
        )
        Text(text = "$expenseDate")
        Button(
            onClick = onButtonClick,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = "Select date")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormDescription(
    expenseUiState: ExpenseUiState,
    onExpenseValueChange: (ExpenseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Menu,
            contentDescription = null
        )
        TextField(
            value = expenseUiState.description,
            onValueChange = { onExpenseValueChange(expenseUiState.copy(description = it)) },
            label = { Text("Enter expense description") },
            modifier = modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormKind(
    expenseUiState: ExpenseUiState,
    onExpenseValueChange: (ExpenseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Menu,
            contentDescription = null
        )
        TextField(
            value = expenseUiState.kind,
            onValueChange = { onExpenseValueChange(expenseUiState.copy(kind = it)) },
            label = { Text("Enter expense kind") },
            modifier = modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormPrice(
    expenseUiState: ExpenseUiState,
    onExpenseValueChange: (ExpenseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Menu,
            contentDescription = null
        )
        TextField(
            value = expenseUiState.price,
            onValueChange = { onExpenseValueChange(expenseUiState.copy(price = it)) },
            label = { Text("Enter expense price") },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun ExpenseFormSaveButton(
    onClick: () -> Unit,
    expenseUiState: ExpenseUiState
    ) {
    Button(
        onClick = onClick,
        enabled = expenseUiState.actionEnabled
    ) {
        Text(text = "Save")
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormDatePreview() {
    ExpensesTheme {
        var expenseDate by rememberSaveable { mutableStateOf("") }
        ExpenseFormDate(
            expenseDate = expenseDate,
            onButtonClick = { expenseDate = "01/01/2023" })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormDescriptionPreview() {
    ExpensesTheme {
        ExpenseFormDescription(
            ExpenseUiState(),
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormKindPreview() {
    ExpensesTheme {
        ExpenseFormKind(
            ExpenseUiState(),
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormPricePreview() {
    ExpensesTheme {
        ExpenseFormPrice(
            ExpenseUiState(),
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormSaveButtonPreview() {
    ExpensesTheme {
        ExpenseFormSaveButton(
            onClick = {},
            ExpenseUiState()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseScreenBodyPreview() {
    ExpensesTheme {
        ExpenseScreenBody(
            expenseDate = "",
            onExpenseFormSaveButtonClick = {},
            datePickerDialog = DatePickerDialog(LocalContext.current),
            expenseUiState = ExpenseUiState(),
            onExpenseValueChange = {},
            expenses = listOf(
                Expense(id=1, date="2023/01/01", description="description", kind="kind", price=1.0),
                Expense(id=2, date="2023/01/01", description="description", kind="kind", price=1.0)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseBottomNavigationBarPreview() {
    var expensesEnabled by rememberSaveable { mutableStateOf(false) }
    ExpensesTheme {
        ExpenseBottomNavigationBar(
            onExpensesClicked = { expensesEnabled = true },
            onResultsClicked = { expensesEnabled = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsPagePreview() {
    ResultsScreen()
}
