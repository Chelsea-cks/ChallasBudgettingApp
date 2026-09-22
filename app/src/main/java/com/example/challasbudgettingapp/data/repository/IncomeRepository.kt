package com.example.challasbudgettingapp.data.repository

import com.example.challasbudgettingapp.data.api.ApiService
import com.example.challasbudgettingapp.data.api.RetrofitClient
import com.example.challasbudgettingapp.data.model.Income

class IncomeRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    suspend fun getIncome(): List<Income> {
        return apiService.getIncome()
    }

    suspend fun getIncomeById(id: Int): Income {
        return apiService.getIncomeById(id)
    }

    suspend fun createIncome(income: Income): Income {
        val response = apiService.createIncome(income)

        if (response.isSuccessful) {
            return response.body()
                ?: throw Exception("Empty response from server.")
        }

        throw Exception("Server error: ${response.code()}")
    }

    suspend fun updateIncome(
        id: Int,
        income: Income
    ) {
        val response = apiService.updateIncome(id, income)

        if (!response.isSuccessful) {
            throw Exception("Server error: ${response.code()}")
        }
    }

    suspend fun deleteIncome(id: Int) {
        val response = apiService.deleteIncome(id)

        if (!response.isSuccessful) {
            throw Exception("Server error: ${response.code()}")
        }
    }
}