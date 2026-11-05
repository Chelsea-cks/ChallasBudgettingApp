
package com.example.challasbudgettingapp.data.repository

import com.example.challasbudgettingapp.data.api.RetrofitClient
import com.example.challasbudgettingapp.data.model.SavingsGoal

class SavingsGoalRepository {

    private val apiService = RetrofitClient.apiService

    // Retrieve savings goals from MongoDB through the API
    suspend fun getSavingsGoals(userId: String): List<SavingsGoal> {
        return apiService.getSavingsGoals(userId)
    }

    // Save a new savings goal to MongoDB through the API
    suspend fun createSavingsGoal(goal: SavingsGoal): SavingsGoal {
        val response = apiService.createSavingsGoal(goal)

        if (response.isSuccessful) {
            return response.body()
                ?: throw Exception("The saved savings goal could not be retrieved.")
        } else {
            throw Exception(
                "Failed to save savings goal. Error code: ${response.code()}"
            )
        }
    }
}
