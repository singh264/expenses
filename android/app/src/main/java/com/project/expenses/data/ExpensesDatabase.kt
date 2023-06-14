package com.project.expenses.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Expense::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class ExpensesDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var Instance: ExpensesDatabase? = null

        fun getDatabase(context: Context): ExpensesDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ExpensesDatabase::class.java,
                    "expense_database"
                )
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
