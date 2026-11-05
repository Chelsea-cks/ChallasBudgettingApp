
package com.example.challasbudgettingapp.data.repository

import com.example.challasbudgettingapp.data.api.ApiService
import com.example.challasbudgettingapp.data.api.RetrofitClient
import com.example.challasbudgettingapp.data.model.Expense
import okhttp3.MultipartBody

class ExpenseRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    suspend fun getExpenses(): List<Expense> {
        return apiService.getExpenses()
    }

    suspend fun getExpense(id: Int): Expense {
        return apiService.getExpense(id)
    }

    suspend fun createExpense(expense: Expense): Expense {
        val response = apiService.createExpense(expense)

        if (response.isSuccessful) {
            return response.body()
                ?: throw Exception("Empty response from server.")
        }

        throw Exception("Server error: ${response.code()}")
    }

    @Suppress("unused")
    suspend fun uploadReceipt(
        image: MultipartBody.Part
    ): String {
        return uploadAzureReceipt(image)
    }

    suspend fun uploadAzureReceipt(
        image: MultipartBody.Part
    ): String {
        val response = apiService.uploadAzureReceipt(image)

        if (response.isSuccessful) {
            return response.body()?.imageUrl
                ?: throw Exception("Azure receipt upload returned an empty URL.")
        }

        throw Exception("Azure receipt upload failed: ${response.code()}")
    }

    suspend fun updateExpense(
        id: Int,
        expense: Expense
    ) {
        val response = apiService.updateExpense(id, expense)

        if (!response.isSuccessful) {
            throw Exception("Server error: ${response.code()}")
        }
    }

    suspend fun deleteExpense(id: Int) {
        val response = apiService.deleteExpense(id)

        if (!response.isSuccessful) {
            throw Exception("Server error: ${response.code()}")
        }
    }
}
