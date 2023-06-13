package com.example.expenses.ui.expenses

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenses.ui.AppViewModelProvider
import com.example.expenses.ui.theme.ExpensesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailsScreen(
    navigateBack: () -> Unit,
    viewModel: ExpensesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    var loadedExpense by rememberSaveable { mutableStateOf(false) }
    if (!loadedExpense) {
        viewModel.loadExpense()
        loadedExpense = true
    }
    else {
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        val calendar = Calendar.getInstance()
        var expenseYear by rememberSaveable { mutableStateOf(calendar.get(Calendar.YEAR)) }
        var expenseMonth by rememberSaveable { mutableStateOf(calendar.get(Calendar.MONTH)) }
        var expenseDay by rememberSaveable { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
        var expenseDate by rememberSaveable { mutableStateOf(viewModel.expenseUiState.date) }
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
                    title = { Text(text = "Expense Edit") },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
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
            ExpenseDetailsBody(
                expenseDate = expenseDate,
                onExpenseFormSaveButtonClick =  {
                    coroutineScope.launch {
                        viewModel.updateExpense()
                        viewModel.resetUiState()
                        expenseDate = ""
                        navigateBack()
                    }
                },
                datePickerDialog = datePickerDialog,
                expenseUiState = viewModel.expenseUiState,
                onExpenseValueChange = viewModel::updateUiState,
                onExpenseFormDeleteButtonClick = {
                    coroutineScope.launch {
                        viewModel.deleteExpense()
                        viewModel.resetUiState()
                        expenseDate = ""
                        navigateBack()
                    }
                },
                modifier = modifier.padding(padding)
            )
        }
    }
}

@Composable
fun ExpenseDetailsBody(
    expenseDate: String,
    onExpenseFormSaveButtonClick: () -> Unit,
    datePickerDialog: DatePickerDialog,
    expenseUiState: ExpenseUiState,
    onExpenseValueChange: (ExpenseUiState) -> Unit,
    onExpenseFormDeleteButtonClick: () -> Unit,
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
        ExpenseFormDeleteButton(
            onClick = onExpenseFormDeleteButtonClick,
            expenseUiState = expenseUiState
        )
    }
}

@Composable
fun ExpenseFormDeleteButton(
    onClick: () -> Unit,
    expenseUiState: ExpenseUiState,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = expenseUiState.actionEnabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Delete")
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseDetailsBodyPreview() {
    ExpensesTheme {
        ExpenseDetailsBody(
            expenseDate = "",
            onExpenseFormSaveButtonClick = {},
            datePickerDialog = DatePickerDialog(LocalContext.current),
            expenseUiState = ExpenseUiState(),
            onExpenseValueChange = {},
            onExpenseFormDeleteButtonClick = {}
        )
    }
}

@Preview
@Composable
fun ExpenseFormDeleteButtonPreview() {
    ExpensesTheme {
       ExpenseFormDeleteButton(
           onClick = {},
           expenseUiState = ExpenseUiState()
       )
    }
}
