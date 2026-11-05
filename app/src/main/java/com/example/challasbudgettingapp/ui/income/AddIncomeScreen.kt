package com.example.challasbudgettingapp.ui.income

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.util.Log
import com.example.challasbudgettingapp.util.Validators
import com.example.challasbudgettingapp.data.model.Income
import com.example.challasbudgettingapp.data.offline.OfflineSyncManager
import com.example.challasbudgettingapp.data.repository.IncomeRepository
import kotlinx.coroutines.launch

@Composable
fun AddIncomeScreen(
    userID: Int,
    onSaved: () -> Unit
) {
    val incomeRepository = remember { IncomeRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var amount by rememberSaveable { mutableStateOf("") }
    var source by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Add Income",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (R)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = source,
            onValueChange = { source = it },
            label = { Text("Income Source") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                // Validate input so bad data never reaches the API (no crashes).
                val parsedAmount = Validators.parsePositiveAmount(amount)
                when {
                    userID <= 0 -> errorMessage = "Your session is invalid. Please log in again."
                    parsedAmount == null -> errorMessage = "Enter an amount greater than 0."
                    source.isBlank() -> errorMessage = "Please enter an income source."
                    !Validators.isValidDate(date) -> errorMessage = "Enter the date as yyyy-MM-dd."
                    else -> {
                        errorMessage = ""
                        val income = Income(
                            userID = userID,
                            source = source.trim(),
                            amount = parsedAmount,
                            date = date.trim(),
                            notes = notes
                        )
                        scope.launch {
                            try {
                                incomeRepository.createIncome(income)
                                Log.d("AddIncome", "Income saved online")
                            } catch (e: Exception) {
                                // No connection or server error: queue for later sync.
                                Log.w("AddIncome", "Saving offline: ${e.message}")
                                OfflineSyncManager(context).saveOfflineIncome(income)
                            }
                            onSaved()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Income")
        }
    }
}
