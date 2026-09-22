package com.example.challasbudgettingapp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.challasbudgettingapp.data.repository.UserRepository

@Composable
fun DashboardScreen(
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit,
    onReports: () -> Unit,
    onSettings: () -> Unit
) {
    val expenseRepository = remember {
        ExpenseRepository()
    }

    val incomeRepository = remember {
        IncomeRepository()
    }

    val userRepository = remember {
        UserRepository()
    }

    var totalIncome by remember {
        mutableDoubleStateOf(0.0)
    }

    var totalExpenses by remember {
        mutableDoubleStateOf(0.0)
    }

    LaunchedEffect(Unit) {

        // Test/load users
        try {
            userRepository.getUsers()
            userRepository.getUser(1)
        } catch (_: Exception) {
            // Keep the dashboard running if the API is unavailable.
        }

        // Load expenses
        try {
            val expenses = expenseRepository.getExpenses()

            totalExpenses = expenses.sumOf { expense ->
                expense.amount
            }

        } catch (_: Exception) {
            totalExpenses = 0.0
        }

        // Load income
        try {
            val incomes = incomeRepository.getIncome()

            totalIncome = incomes.sumOf { income ->
                income.amount
            }

        } catch (_: Exception) {
            totalIncome = 0.0
        }
    }

    val balance = totalIncome - totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Challas Budget",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Take Control of Every Rand"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Current Balance"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "R%.2f".format(balance),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Monthly Budget"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "R%.2f".format(totalIncome),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = onAddExpense,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Expense"
                )
            }

            Button(
                onClick = onAddIncome,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Income"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = onReports,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "View Reports"
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = onSettings,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Settings"
            )
        }
    }
}