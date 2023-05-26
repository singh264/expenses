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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.expenses.ui.theme.ExpensesTheme
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
fun App() {
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
                ExpenseForm()
            }
            else {
                ResultsPage()
            }

        }
    }
}

@Composable
fun ExpenseBottomNavigationBar(
    onExpensesClicked: () -> Unit,
    onResultsClicked: () -> Unit,
    modifier: Modifier = Modifier,
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
fun ResultsPage(modifier: Modifier = Modifier) {
    Text(text = "Results page")
}

@Composable
fun ExpenseForm() {
    val calendar = Calendar.getInstance()
    var expenseYear by rememberSaveable { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var expenseMonth by rememberSaveable { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var expenseDay by rememberSaveable { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var expenseDate by rememberSaveable { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        {_: DatePicker, expenseYear: Int, expenseMonth: Int, expenseDay: Int ->
            expenseDate = "$expenseDay/$expenseMonth/$expenseYear"
        }, expenseYear, expenseMonth, expenseDay
    )
    var expenseDescription by rememberSaveable { mutableStateOf("") }
    var expenseKind by rememberSaveable { mutableStateOf("") }
    var expensePrice by rememberSaveable { mutableStateOf("") }
    var shouldShowExpenseDetails by rememberSaveable { mutableStateOf(false) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpenseFormDate(
            expenseDate = expenseDate,
            onButtonClick = { datePickerDialog.show() }
        )
        ExpenseFormDescription(
            expenseDescription = expenseDescription,
            onValueChange = { expenseDescription = it }
        )
        ExpenseFormKind(
            expenseKind = expenseKind,
            onValueChange = { expenseKind = it }
        )
        ExpenseFormPrice(
            expensePrice = expensePrice,
            onValueChange = { expensePrice = it }
        )
        ExpenseFormContinueButton(onClick = { shouldShowExpenseDetails = !shouldShowExpenseDetails })
        if (shouldShowExpenseDetails) {
            Text(text = "Expense date: $expenseDate")
            Text(text = "Expense description: $expenseDescription")
            Text(text = "Expense kind: $expenseKind")
            Text(text = "Expense price: $expensePrice")
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
            Text(text = "Open date picker")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormDescription(
    expenseDescription: String,
    onValueChange: (String) -> Unit,
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
            value = expenseDescription,
            onValueChange = onValueChange,
            label = { Text("Enter expense description") },
            modifier = modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormKind(
    expenseKind: String,
    onValueChange: (String) -> Unit,
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
            value = expenseKind,
            onValueChange = onValueChange,
            label = { Text("Enter expense kind") },
            modifier = modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormPrice(
    expensePrice: String,
    onValueChange: (String) -> Unit,
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
            value = expensePrice,
            onValueChange = onValueChange,
            label = { Text("Enter expense price") },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun ExpenseFormContinueButton(
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = "Continue")
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ExpensesTheme {
        App()
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseDatePreview() {
    ExpensesTheme {
        var expenseDate by rememberSaveable { mutableStateOf("") }
        ExpenseFormDate(
            expenseDate = expenseDate,
            onButtonClick = { expenseDate = "01/01/2023" })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseDescriptionPreview() {
    ExpensesTheme {
        var expenseDescription by rememberSaveable { mutableStateOf("") }
        ExpenseFormDescription(
            expenseDescription = expenseDescription,
            onValueChange = { expenseDescription = it })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseKindPreview() {
    ExpensesTheme {
        var expenseKind by rememberSaveable { mutableStateOf("") }
        ExpenseFormKind(
            expenseKind = expenseKind,
            onValueChange = { expenseKind = it })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpensePricePreview() {
    ExpensesTheme {
        var expensePrice by rememberSaveable { mutableStateOf("") }
        ExpenseFormPrice(
            expensePrice = expensePrice,
            onValueChange = { expensePrice = it })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormContinueButtonPreview() {
    ExpensesTheme {
        var shouldShowExpenseDetails by rememberSaveable { mutableStateOf(false) }
        ExpenseFormContinueButton(onClick = {
            shouldShowExpenseDetails = !shouldShowExpenseDetails
        })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormPreview() {
    ExpensesTheme {
        ExpenseForm()
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
    ResultsPage()
}