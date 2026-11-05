package com.example.challasbudgettingapp.data.model

data class Income(
    val incomeID: Int = 0,
    val userID: Int,
    val amount: Double,
    val source: String,
    val date: String,
    val notes: String? = null
)