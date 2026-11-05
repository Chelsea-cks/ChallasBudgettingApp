
package com.example.challasbudgettingapp.data.model

data class SavingsGoal(
    val id: String? = null,
    val userId: String,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: String? = null,
    val createdAt: String? = null
)
