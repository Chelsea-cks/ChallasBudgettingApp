package com.example.challasbudgettingapp.data.repository

import com.example.challasbudgettingapp.data.api.RetrofitClient

class ApiTestRepository {

    suspend fun testConnection(): Result<String> {
        return try {
            val users = RetrofitClient.apiService.getUsers()

            Result.success(
                "API connected successfully. Users found: ${users.size}"
            )
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    e.message ?: "Unable to connect to API."
                )
            )
        }
    }
}