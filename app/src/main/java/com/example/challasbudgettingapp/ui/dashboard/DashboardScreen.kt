
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
import androidx.compose.ui.res.stringResource
import com.example.challasbudgettingapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.util.Log
import com.example.challasbudgettingapp.data.offline.OfflineSyncManager
import com.example.challasbudgettingapp.data.repository.ExpenseRepository
import com.example.challasbudgettingapp.data.repository.IncomeRepository
import kotlinx.coroutines.CancellationException
import java.util.Locale

@Composable
fun DashboardScreen(
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit,
    onReports: () -> Unit,
    onSettings: () -> Unit,
    onSavingsGoals: () -> Unit
) {
    val context = LocalContext.current

    var totalIncome by remember {
        mutableDoubleStateOf(0.0)
    }

    var totalExpenses by remember {
        mutableDoubleStateOf(0.0)
    }

    LaunchedEffect(Unit) {
        // Synchronise any saved offline data.
        try {
            OfflineSyncManager(context).syncPendingData()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("BudgetQuestDashboard", "Offline sync failed", e)
        }

        // Load expenses.
        try {
            val expenses = ExpenseRepository().getExpenses()
            totalExpenses = expenses.sumOf { it.amount }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("BudgetQuestDashboard", "Could not load expenses", e)
        }

        // Load income.
        try {
            val incomes = IncomeRepository().getIncome()
            totalIncome = incomes.sumOf { it.amount }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("BudgetQuestDashboard", "Could not load income", e)
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

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = stringResource(R.string.motto))

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = stringResource(R.string.current_balance))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "R${String.format(Locale.getDefault(), "%.2f", balance)}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = stringResource(R.string.monthly_budget))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "R${String.format(Locale.getDefault(), "%.2f", totalIncome)}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onAddExpense,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.expense))
            }

            Button(
                onClick = onAddIncome,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.income))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSavingsGoals,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.savings_goals))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onReports,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.view_reports))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings))
        }
    }
}
