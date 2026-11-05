
package com.example.challasbudgettingapp.ui.savings

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.challasbudgettingapp.data.model.SavingsGoal
import com.example.challasbudgettingapp.data.repository.SavingsGoalRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalsScreen(
    userId: String,
    onBack: () -> Unit
) {
    val repository = remember { SavingsGoalRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("") }
    var targetAmount by rememberSaveable { mutableStateOf("") }
    var currentAmount by rememberSaveable { mutableStateOf("0") }
    var targetDate by rememberSaveable { mutableStateOf("") }

    var goals by remember { mutableStateOf<List<SavingsGoal>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    suspend fun loadGoals() {
        try {
            isLoading = true
            errorMessage = null
            goals = repository.getSavingsGoals(userId)
        } catch (e: Exception) {
            errorMessage = "Could not load savings goals: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(userId) {
        loadGoals()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Savings Goals") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text("Create a savings goal")

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it },
                label = { Text("Target amount (R)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = currentAmount,
                onValueChange = { currentAmount = it },
                label = { Text("Amount saved so far (R)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TARGET DATE CALENDAR
            OutlinedButton(
                onClick = {
                    val calendar = Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            targetDate = String.format(
                                Locale.US,
                                "%04d-%02d-%02d",
                                year,
                                month + 1,
                                dayOfMonth
                            )
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = System.currentTimeMillis()
                    }.show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (targetDate.isBlank()) {
                        "Select Target Date"
                    } else {
                        "Target Date: $targetDate"
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    errorMessage = null
                    successMessage = null

                    val target = targetAmount.toDoubleOrNull()
                    val current = currentAmount.toDoubleOrNull()

                    when {
                        title.isBlank() -> {
                            errorMessage = "Please enter a goal name."
                        }

                        target == null || target <= 0 -> {
                            errorMessage = "Enter a valid target amount."
                        }

                        current == null || current < 0 -> {
                            errorMessage = "Enter a valid saved amount."
                        }

                        current > target -> {
                            errorMessage = "Saved amount cannot exceed the target."
                        }

                        else -> {
                            scope.launch {
                                try {
                                    isSaving = true

                                    val goal = SavingsGoal(
                                        userId = userId,
                                        title = title.trim(),
                                        targetAmount = target,
                                        currentAmount = current,
                                        targetDate = if (targetDate.isBlank()) {
                                            null
                                        } else {
                                            "${targetDate}T00:00:00"
                                        }
                                    )

                                    repository.createSavingsGoal(goal)

                                    title = ""
                                    targetAmount = ""
                                    currentAmount = "0"
                                    targetDate = ""

                                    successMessage =
                                        "Savings goal saved successfully!"

                                    loadGoals()

                                } catch (e: Exception) {
                                    errorMessage =
                                        "Could not save goal: ${e.message}"
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            ) {
                Text(
                    if (isSaving) "Saving..." else "Save Savings Goal"
                )
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it)
            }

            successMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Your savings goals")

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (goals.isEmpty()) {
                Text("You haven't created any savings goals yet.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(goals) { goal ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(goal.title)

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    "Saved: R ${"%.2f".format(goal.currentAmount)}"
                                )

                                Text(
                                    "Target: R ${"%.2f".format(goal.targetAmount)}"
                                )

                                val progress =
                                    (goal.currentAmount / goal.targetAmount)
                                        .toFloat()
                                        .coerceIn(0f, 1f)

                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                )

                                Text("${(progress * 100).toInt()}% completed")

                                goal.targetDate?.let {
                                    Text("Target date: ${it.take(10)}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
