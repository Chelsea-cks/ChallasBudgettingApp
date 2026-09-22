package com.example.challasbudgettingapp.data.api

import com.example.challasbudgettingapp.data.model.Expense
import com.example.challasbudgettingapp.data.model.Income
import com.example.challasbudgettingapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {


    // USERS

    @GET("api/Users")
    suspend fun getUsers(): List<User>

    @GET("api/Users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): User

    @POST("api/Users")
    suspend fun createUser(
        @Body user: User
    ): Response<User>



    // EXPENSES


    @GET("api/Expenses")
    suspend fun getExpenses(): List<Expense>

    @GET("api/Expenses/{id}")
    suspend fun getExpense(
        @Path("id") id: Int
    ): Expense

    @POST("api/Expenses")
    suspend fun createExpense(
        @Body expense: Expense
    ): Response<Expense>

    @PUT("api/Expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: Int,
        @Body expense: Expense
    ): Response<Unit>

    @DELETE("api/Expenses/{id}")
    suspend fun deleteExpense(
        @Path("id") id: Int
    ): Response<Unit>



    // INCOME


    @GET("api/Income")
    suspend fun getIncome(): List<Income>

    @GET("api/Income/{id}")
    suspend fun getIncomeById(
        @Path("id") id: Int
    ): Income

    @POST("api/Income")
    suspend fun createIncome(
        @Body income: Income
    ): Response<Income>

    @PUT("api/Income/{id}")
    suspend fun updateIncome(
        @Path("id") id: Int,
        @Body income: Income
    ): Response<Unit>

    @DELETE("api/Income/{id}")
    suspend fun deleteIncome(
        @Path("id") id: Int
    ): Response<Unit>

    // RECEIPT IMAGE UPLOAD

    @Multipart
    @POST("api/Receipt/upload")
    suspend fun uploadReceipt(
        @Part image: MultipartBody.Part
    ): Response<ReceiptUploadResponse>
}
data class ReceiptUploadResponse(
    val message: String,
    val imageUrl: String
)