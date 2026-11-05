package com.example.challasbudgettingapp.data.model

data class Expense(
    val expenseID: Int = 0,
    val userID: Int,
    val category: String,
    val amount: Double,
    val date: String,
    val notes: String? = null,
    val receiptImageUrl: String? = null
)