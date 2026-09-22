package com.example.challasbudgettingapp.data.repository

import android.util.Log
import com.example.challasbudgettingapp.data.api.ApiService
import com.example.challasbudgettingapp.data.api.RetrofitClient
import com.example.challasbudgettingapp.data.model.User

class UserRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }

    suspend fun getUser(id: Int): User {
        return apiService.getUser(id)
    }

    suspend fun createUser(user: User): User {

        Log.d(
            "UserRepository",
            "========== CREATE USER START =========="
        )

        Log.d(
            "UserRepository",
            "User being sent to API: $user"
        )

        val response = apiService.createUser(user)

        Log.d(
            "UserRepository",
            "API response received"
        )

        Log.d(
            "UserRepository",
            "Response code: ${response.code()}"
        )

        Log.d(
            "UserRepository",
            "Response message: ${response.message()}"
        )

        if (response.isSuccessful) {

            val savedUser = response.body()

            Log.d(
                "UserRepository",
                "User saved successfully: $savedUser"
            )

            Log.d(
                "UserRepository",
                "========== CREATE USER END =========="
            )

            return savedUser
                ?: throw Exception("API returned an empty user.")
        }

        val errorBody = response.errorBody()?.string()

        Log.e(
            "UserRepository",
            "========== API ERROR =========="
        )

        Log.e(
            "UserRepository",
            "Error code: ${response.code()}"
        )

        Log.e(
            "UserRepository",
            "Error message: ${response.message()}"
        )

        Log.e(
            "UserRepository",
            "Error body: $errorBody"
        )

        throw Exception(
            "API error ${response.code()}: $errorBody"
        )
    }
}