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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit,
    onReports: () -> Unit,
    onSettings: () -> Unit
) {

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
                    text = "R0.00",
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
                    text = "R0.00",
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