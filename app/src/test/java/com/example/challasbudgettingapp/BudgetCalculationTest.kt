package com.example.challasbudgettingapp

import org.junit.Assert.assertEquals
import org.junit.Test

class BudgetCalculationTest {

    @Test
    fun calculateBalance_incomeMinusExpenses() {

        val income = 5000.0
        val expenses = 2000.0

        val balance = income - expenses

        assertEquals(3000.0, balance, 0.01)
    }

    @Test
    fun calculateBalance_whenExpensesGreaterThanIncome() {

        val income = 2000.0
        val expenses = 2500.0

        val balance = income - expenses

        assertEquals(-500.0, balance, 0.01)
    }

    @Test
    fun calculateTotalExpenses() {

        val expenses = listOf(
            100.0,
            250.0,
            150.0
        )

        val total = expenses.sum()

        assertEquals(500.0, total, 0.01)
    }

    @Test
    fun calculateTotalIncome() {

        val income = listOf(
            5000.0,
            1000.0
        )

        val total = income.sum()

        assertEquals(6000.0, total, 0.01)
    }
}