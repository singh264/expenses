package com.project.expenses.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val description: String,
    val kind: String,
    val price: Double,
    @ColumnInfo(defaultValue = "0")
    val isIncome: Boolean
)
