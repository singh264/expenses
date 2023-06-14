package com.project.expenses.ui.results

import android.graphics.Typeface
import androidx.compose.ui.graphics.toArgb
import com.project.expenses.data.Expense
import com.project.expenses.ui.theme.blueColor
import com.project.expenses.ui.theme.greenColor
import com.project.expenses.ui.theme.redColor
import com.project.expenses.ui.theme.whiteColor
import com.project.expenses.ui.theme.yellowColor
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.ArrayList
import java.util.LinkedHashMap

data class ResultsData (
    var expenseKind: String,
    var value: Float
)

fun updatePieChartWithData(
    chart: PieChart,
    data: List<Expense>
) {
    val pieChartData = getPieChartData(data)
    val entries = ArrayList<PieEntry>()
    for (i in pieChartData.indices) {
        val pieChartDatum = pieChartData[i]
        entries.add(PieEntry(pieChartDatum.value, pieChartDatum.expenseKind))
    }

    val ds = PieDataSet(entries, "")
    ds.colors = arrayListOf(
        greenColor.toArgb(),
        blueColor.toArgb(),
        redColor.toArgb(),
        yellowColor.toArgb(),
    )
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.sliceSpace = 2f
    ds.valueTextColor = whiteColor.toArgb()
    ds.valueTextSize = 18f
    ds.valueTypeface = Typeface.DEFAULT_BOLD

    val d = PieData(ds)
    chart.data = d
    chart.invalidate()
}

fun getPieChartData(expenses: List<Expense>): List<ResultsData> {
    val pieChartData = ArrayList<ResultsData>()
    val expenseKindPricesMap = LinkedHashMap<String, Float>()
    var totalExpensePrice = 0f

    for (i in expenses.indices) {
        val expense = expenses[i]
        val expensePrice = expense.price
        val expenseKind = expense.kind
        totalExpensePrice += expense.price.toFloat()
        if (expenseKindPricesMap.containsKey(expenseKind)) {
            val expenseKindPrice = expenseKindPricesMap.get(expenseKind)
            if (expenseKindPrice != null) {
                expenseKindPricesMap.put(expenseKind, expenseKindPrice + expensePrice.toFloat())
            };
        }
        else {
            expenseKindPricesMap.put(expenseKind, expensePrice.toFloat());
        }
    }

    for (expenseKindPricesMapEntry in expenseKindPricesMap.entries.iterator()) {
        val expenseKind = expenseKindPricesMapEntry.key
        val expenseKindPrice = expenseKindPricesMapEntry.value
        val expenseKindPricePercentage = (expenseKindPrice / totalExpensePrice) * 100
        pieChartData.add(ResultsData(expenseKind, expenseKindPricePercentage))
    }

    return pieChartData
}

fun getExpensesSummaryData(expenses: List<Expense>): ArrayList<ResultsData> {
    val expensesSummaryData = ArrayList<ResultsData>()
    val expenseKindPricesMap = LinkedHashMap<String, Float>()

    for (i in expenses.indices) {
        val expense = expenses[i]
        val isIncome = expense.isIncome
        var expensePrice = expense.price
        if (!isIncome) {
            expensePrice *= -1
        }
        val expenseKind = expense.kind
        if (expenseKindPricesMap.containsKey(expenseKind)) {
            val expenseKindPrice = expenseKindPricesMap.get(expenseKind)
            if (expenseKindPrice != null) {
                expenseKindPricesMap.put(expenseKind, expenseKindPrice + expensePrice.toFloat())
            };
        }
        else {
            expenseKindPricesMap.put(expenseKind, expensePrice.toFloat());
        }
    }

    for (expenseKindPricesMapEntry in expenseKindPricesMap.entries.iterator()) {
        val expenseKind = expenseKindPricesMapEntry.key
        val expenseKindPrice = expenseKindPricesMapEntry.value
        expensesSummaryData.add(ResultsData(expenseKind, expenseKindPrice))
    }

    return expensesSummaryData
}

fun getNetIncome(expenses: List<Expense>): Float {
    val expensesSummaryData = ArrayList<ResultsData>()
    val expenseKindPricesMap = LinkedHashMap<String, Float>()
    var netIncome = 0f

    for (i in expenses.indices) {
        val expense = expenses[i]
        val expensePrice = expense.price
        val isIncome = expense.isIncome
        if (isIncome) {
            netIncome += expensePrice.toFloat()
        } else {
            netIncome -= expensePrice.toFloat()
        }
    }

    return netIncome
}