package com.example.expenses.ui.expense

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import com.example.expenses.ui.AppViewModelProvider
import com.example.expenses.ui.theme.ExpensesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    onNavigateUp: () -> Unit,
    viewModel: ExpenseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
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
        { _: DatePicker, expenseYear: Int, expenseMonth: Int, expenseDay: Int ->
            expenseDate = "$expenseDay/$expenseMonth/$expenseYear"
        }, expenseYear, expenseMonth, expenseDay
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Expense Entry") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = modifier
            )
        }
    ) { padding ->
        ExpenseEntryBody(
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
            modifier = modifier.padding(padding)
        )
    }
}

@Composable
fun ExpenseEntryBody(
    expenseDate: String,
    onExpenseFormSaveButtonClick: () -> Unit,
    datePickerDialog: DatePickerDialog,
    expenseUiState: ExpenseUiState,
    onExpenseValueChange: (ExpenseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
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
        ExpenseFormIsIncome(
            expenseUiState = expenseUiState,
            onExpenseValueChange = onExpenseValueChange
        )
        ExpenseFormSaveButton(
            onClick = onExpenseFormSaveButtonClick,
            expenseUiState = expenseUiState,
        )
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
fun ExpenseFormIsIncome(
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
        Text(text = "Is Income?")
        Checkbox(
            checked = expenseUiState.isIncome,
            onCheckedChange = { onExpenseValueChange(expenseUiState.copy(isIncome = it)) }
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
fun ExpenseScreenBodyPreview() {
    ExpensesTheme {
        ExpenseEntryBody(
            expenseDate = "",
            onExpenseFormSaveButtonClick = {},
            datePickerDialog = DatePickerDialog(LocalContext.current),
            expenseUiState = ExpenseUiState(),
            onExpenseValueChange = {}
        )
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
fun ExpenseFormIsIncomePreview() {
    ExpensesTheme {
        ExpenseFormIsIncome(
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
