package com.example.challasbudgettingapp.util

/** Budget calculations shared by the dashboard and reports. */
object BudgetMath {
    fun balance(totalIncome: Double, totalExpenses: Double) = totalIncome - totalExpenses

    /** Percentage of a savings goal reached, clamped to 0..100. */
    fun goalProgress(current: Double, target: Double): Double =
        if (target <= 0.0) 0.0 else (current / target * 100.0).coerceIn(0.0, 100.0)
}
