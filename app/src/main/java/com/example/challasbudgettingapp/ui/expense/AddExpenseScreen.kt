
package com.example.challasbudgettingapp.ui.expense

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.challasbudgettingapp.data.model.Expense
import com.example.challasbudgettingapp.data.offline.OfflineSyncManager
import com.example.challasbudgettingapp.data.repository.ExpenseRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun AddExpenseScreen(
    userID: Int,
    onSaved: () -> Unit
){
    val expenseRepository = remember {
        ExpenseRepository()
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var amount by rememberSaveable {
        mutableStateOf("")
    }

    var category by rememberSaveable {
        mutableStateOf("")
    }

    var date by rememberSaveable {
        mutableStateOf("")
    }

    var notes by rememberSaveable {
        mutableStateOf("")
    }

    var showDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()

    var selectedReceiptUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    var errorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var isSaving by rememberSaveable {
        mutableStateOf(false)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedReceiptUri = uri
        errorMessage = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // AMOUNT
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                errorMessage = ""
            },
            label = {
                Text("Amount (R)")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isSaving
        )

        Spacer(modifier = Modifier.height(12.dp))

        // CATEGORY
        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
                errorMessage = ""
            },
            label = {
                Text("Category")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isSaving
        )

        Spacer(modifier = Modifier.height(12.dp))

        // DATE - MANUAL ENTRY OR CALENDAR PICKER
        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
                errorMessage = ""
            },
            label = {
                Text("Date")
            },
            placeholder = {
                Text("yyyy-MM-dd")
            },
            trailingIcon = {
                TextButton(
                    onClick = {
                        showDatePicker = true
                    },
                    enabled = !isSaving
                ) {
                    Text("Calendar")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isSaving
        )

        // CALENDAR POP-UP
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDate =
                                datePickerState.selectedDateMillis

                            if (selectedDate != null) {
                                val formatter = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.US
                                )

                                formatter.timeZone =
                                    TimeZone.getTimeZone("UTC")

                                date = formatter.format(
                                    Date(selectedDate)
                                )

                                errorMessage = ""
                            }

                            showDatePicker = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // NOTES
        OutlinedTextField(
            value = notes,
            onValueChange = {
                notes = it
                errorMessage = ""
            },
            label = {
                Text("Notes")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            enabled = !isSaving
        )

        Spacer(modifier = Modifier.height(16.dp))

        // RECEIPT UPLOAD BUTTON
        Button(
            onClick = {
                imagePicker.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving
        ) {
            Text("Attach Receipt")
        }

        // SELECTED RECEIPT PREVIEW
        selectedReceiptUri?.let { uri ->

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Selected Receipt",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected receipt image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }

        // ERROR MESSAGE
        if (errorMessage.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SAVE EXPENSE BUTTON
        Button(
            onClick = {

                val parsedAmount = amount.toDoubleOrNull()

                when {

                    userID <= 0 -> {
                        errorMessage =
                            "Could not identify your account. Please log in again."
                    }

                    parsedAmount == null || parsedAmount <= 0 -> {
                        errorMessage =
                            "Please enter a valid expense amount."
                    }

                    category.isBlank() -> {
                        errorMessage =
                            "Please enter an expense category."
                    }

                    date.isBlank() -> {
                        errorMessage =
                            "Please enter the expense date."
                    }

                    !date.matches(
                        Regex("\\d{4}-\\d{2}-\\d{2}")
                    ) -> {
                        errorMessage =
                            "Please enter the date as yyyy-MM-dd, for example 2026-11-04."
                    }

                    else -> {

                        isSaving = true
                        errorMessage = ""

                        scope.launch {
                            try {

                                // Upload receipt to AZURE first,
                                // if a receipt was selected.
                                var receiptImageUrl: String? = null

                                selectedReceiptUri?.let { uri ->

                                    val contentResolver =
                                        context.contentResolver

                                    val mimeType =
                                        contentResolver.getType(uri)
                                            ?: "image/jpeg"

                                    val inputStream =
                                        contentResolver.openInputStream(uri)
                                            ?: throw Exception(
                                                "Could not read the selected receipt."
                                            )

                                    val imageBytes =
                                        inputStream.use {
                                            it.readBytes()
                                        }

                                    // Maximum receipt size: 5 MB.
                                    if (imageBytes.size >
                                        5 * 1024 * 1024
                                    ) {
                                        throw Exception(
                                            "Receipt must be 5 MB or smaller."
                                        )
                                    }

                                    // Prepare image for API upload.
                                    val requestBody =
                                        imageBytes.toRequestBody(
                                            mimeType.toMediaTypeOrNull()
                                        )

                                    val fileName =
                                        "receipt_${System.currentTimeMillis()}.jpg"

                                    val imagePart =
                                        MultipartBody.Part.createFormData(
                                            "image",
                                            fileName,
                                            requestBody
                                        )

                                    // NEW: Upload to Azure Blob Storage.
                                    receiptImageUrl =
                                        expenseRepository.uploadAzureReceipt(
                                            imagePart
                                        )
                                }

                                // Create expense using logged-in user ID.
                                val expense = Expense(
                                    userID = userID,
                                    category = category.trim(),
                                    amount = parsedAmount,
                                    date = date.trim(),
                                    notes = notes.trim().ifEmpty {
                                        null
                                    },
                                    receiptImageUrl = receiptImageUrl
                                )

                                // Save expense to the API.
                                expenseRepository.createExpense(expense)

                                isSaving = false

                                // Return to dashboard after successful save.
                                onSaved()

                            } catch (e: Exception) {
                                e.printStackTrace()
                                isSaving = false

                                try {
                                    val offlineManager = OfflineSyncManager(context)
                                    val fallbackExpense = Expense(
                                        userID = userID,
                                        category = category.trim(),
                                        amount = parsedAmount,
                                        date = date.trim(),
                                        notes = notes.trim().ifEmpty { null },
                                        receiptImageUrl = null
                                    )
                                    offlineManager.saveOfflineExpense(fallbackExpense)
                                    onSaved()
                                } catch (_: Exception) {
                                    errorMessage =
                                        e.message
                                            ?: "Something went wrong while saving your expense."
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving
        ) {

            if (isSaving) {

                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    strokeWidth = 2.dp
                )

            } else {

                Text("Save Expense")
            }
        }
    }
}
