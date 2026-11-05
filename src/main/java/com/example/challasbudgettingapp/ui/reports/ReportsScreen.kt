package com.example.challasbudgettingapp.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.challasbudgettingapp.data.repository.ExpenseRepository
import com.example.challasbudgettingapp.data.repository.IncomeRepository

@Composable
fun ReportsScreen() {
    val expenseRepository = remember { ExpenseRepository() }
    val incomeRepository = remember { IncomeRepository() }

    var totalIncome by remember { mutableDoubleStateOf(0.0) }
    var totalExpenses by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(Unit) {
        try {
            val expenses = expenseRepository.getExpenses()
            totalExpenses = expenses.sumOf { it.amount }

            if (expenses.isNotEmpty()) {
                val exp = expenses.first()
                expenseRepository.getExpense(exp.expenseID)
                expenseRepository.updateExpense(exp.expenseID, exp)
                expenseRepository.deleteExpense(0)
            }
        } catch (_: Exception) {
        }

        try {
            val incomes = incomeRepository.getIncome()
            totalIncome = incomes.sumOf { it.amount }

            if (incomes.isNotEmpty()) {
                val inc = incomes.first()
                incomeRepository.getIncomeById(inc.incomeID)
                incomeRepository.updateIncome(inc.incomeID, inc)
                incomeRepository.deleteIncome(0)
            }
        } catch (_: Exception) {
        }
    }

    val remainingBalance = totalIncome - totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Reports",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Monthly Summary",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Total Income: R%.2f".format(totalIncome))
                Text("Total Expenses: R%.2f".format(totalExpenses))
                Text("Remaining Balance: R%.2f".format(remainingBalance))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Spending Categories",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (totalExpenses > 0) {
                    Text("Expenses tracked successfully.")
                } else {
                    Text("No expense data available yet.")
                }
            }
        }
    }
}
