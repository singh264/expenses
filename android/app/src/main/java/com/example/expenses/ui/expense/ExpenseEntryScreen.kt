package com.example.expenses.ui.expense

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.ui.AppViewModelProvider
import com.example.expenses.ui.theme.ExpensesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Currency
import java.util.Locale

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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
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
        OutlinedButton(
            onClick = onButtonClick,
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null
            )
            if (expenseDate.isNotBlank()) {
                Text(
                    text = "$expenseDate",
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth())
            }
            else {
                Text(
                    text = "Select date",
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
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
        OutlinedTextField(
            value = expenseUiState.description,
            onValueChange = { onExpenseValueChange(expenseUiState.copy(description = it)) },
            label = { Text(text = "Expense Description *") },
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
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
        OutlinedTextField(
            value = expenseUiState.kind,
            onValueChange = { onExpenseValueChange(expenseUiState.copy(kind = it)) },
            label = { Text(text = "Expense Kind *") },
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
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
        OutlinedTextField(
            value = expenseUiState.price,
            onValueChange = { onExpenseValueChange(expenseUiState.copy(price = it)) },
            label = { Text(text = "Expense Price *") },
            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
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
        Checkbox(
            checked = expenseUiState.isIncome,
            onCheckedChange = { onExpenseValueChange(expenseUiState.copy(isIncome = it)) }
        )
        Text(
            text = "Is Income?",
            modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExpenseFormSaveButton(
    onClick: () -> Unit,
    expenseUiState: ExpenseUiState,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = expenseUiState.actionEnabled,
        modifier = modifier.fillMaxWidth()
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
fun ExpenseFormDateWithoutDatePreview() {
    ExpensesTheme {
        var expenseDate by rememberSaveable { mutableStateOf("") }
        ExpenseFormDate(
            expenseDate = expenseDate,
            onButtonClick = { expenseDate = "01/01/2023" })
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseFormDateWithDatePreview() {
    ExpensesTheme {
        var expenseDate by rememberSaveable { mutableStateOf("12/5/2023") }
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
