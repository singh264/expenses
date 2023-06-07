package com.example.expenses.ui.expense

import com.example.expenses.data.Expense

data class ExpenseUiState (
    val id: Int = 0,
    val date: String = "",
    val description: String = "",
    val kind: String = "",
    val price: String = "",
    val actionEnabled: Boolean = false
)

fun ExpenseUiState.toExpense(): Expense = Expense(
    id = id,
    date = date,
    description = description,
    kind = kind,
    price = price.toDoubleOrNull()?: 0.0
)

fun ExpenseUiState.isValid(): Boolean {
    return date.isNotBlank() && description.isNotBlank() && kind.isNotBlank() && price.isNotBlank()
}
