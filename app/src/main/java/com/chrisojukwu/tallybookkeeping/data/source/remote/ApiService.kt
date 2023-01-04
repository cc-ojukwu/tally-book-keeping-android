package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkStockItem
import com.chrisojukwu.tallybookkeeping.domain.model.SignInUser
import com.chrisojukwu.tallybookkeeping.domain.model.Token
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.data.dto.network.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //create new user account
    @POST("process-register")
    suspend fun createNewUserAccount(
        @Body userInfo: User
    ): Response<String>

    //login user
    @POST("auth/login")
    suspend fun signInUser(
        @Body userInfo: SignInUser
    ): Response<Token>

    //login google user
    @POST("auth/oauth2/logintest")
    suspend fun googleSignIn(
        @Header("google_id_token") idToken: String
    ): Response<Token>

    @GET("get-all-income")
    suspend fun getAllIncome(): Response<List<NetworkIncome>>

    //insert income
    @POST("insert-income")
    suspend fun insertIncome(
        @Body income: NetworkIncome
    ): Response<String>

    //update income
    @POST("update-income")
    suspend fun updateIncome(
        @Body income: NetworkIncome
    ): Response<String>

    //delete income
    @POST("delete-income")
    suspend fun deleteIncome(
        @Body income: NetworkIncome
    ): Response<String>

    @GET("get-all-expense")
    suspend fun getAllExpense(): Response<List<NetworkExpense>>

    //insert expense
    @POST("insert-expense")
    suspend fun insertExpense(
        @Body expense: NetworkExpense
    ): Response<String>

    //update expense
    @POST("update-expense")
    suspend fun updateExpense(
        @Body expense: NetworkExpense
    ): Response<String>

    //delete income
    @POST("delete-expense")
    suspend fun deleteExpense(
        @Body expense: NetworkExpense
    ): Response<String>

    @GET("get-all-inventory")
    suspend fun getAllInventory(): Response<List<NetworkStockItem>>

    //insert income
    @POST("insert-inventory")
    suspend fun insertInventory(
        @Body stockItem: NetworkStockItem
    ): Response<String>

    //update inventory
    @POST("update-stock")
    suspend fun updateInventory(
        @Body stockItem: NetworkStockItem
    ): Response<String>

    //delete income
    @POST("delete-inventory")
    suspend fun deleteInventory(
        @Body stockItem: NetworkStockItem
    ): Response<String>

}

//use retrofit object to retrieve REST API response
//object Api {
//    val retrofitService: ApiService by lazy {
//        Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
//            .baseUrl(BASE_URL)
//            .build()
//            .create(ApiService::class.java)
//    }
//}