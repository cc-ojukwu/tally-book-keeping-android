package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkStockItem
import com.chrisojukwu.tallybookkeeping.data.dto.network.*
import com.chrisojukwu.tallybookkeeping.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/change-password")
    suspend fun changePassword(
        @Body changePassword: ChangePassword
    ): Response<String>

    @POST("auth/change-email")
    suspend fun changeEmail(
        @Body user: User
    ): Response<TokenWithEmail>

    @POST("auth/email-create-account")
    suspend fun createNewUserAccount(
        @Body userInfo: User
    ): Response<String>

    @POST("auth/email-sign-in")
    suspend fun signInWithEmail(
        @Body userInfo: SignInUser
    ): Response<Token>

    @POST("auth/update-user-info")
    suspend fun updateUserInfo(
        @Body userInfo: User
    ): Response<User>

    @POST("auth/oauth2/google-sign-in")
    suspend fun googleSignIn(
        @Header("google_id_token") idToken: String
    ): Response<TokenWithEmail>

    @GET("user/income/get-all-income")
    suspend fun getAllIncome(): Response<List<NetworkIncome>>

    @POST("user/income/insert-income")
    suspend fun insertIncome(
        @Body income: NetworkIncome
    ): Response<String>

    @POST("user/income/update-income")
    suspend fun updateIncome(
        @Body income: NetworkIncome
    ): Response<String>

    @POST("user/income/delete-income")
    suspend fun deleteIncome(
        @Body income: NetworkIncome
    ): Response<String>

    @GET("user/expense/get-all-expense")
    suspend fun getAllExpense(): Response<List<NetworkExpense>>

    @POST("user/expense/insert-expense")
    suspend fun insertExpense(
        @Body expense: NetworkExpense
    ): Response<String>

    @POST("user/expense/update-expense")
    suspend fun updateExpense(
        @Body expense: NetworkExpense
    ): Response<String>

    @POST("user/expense/delete-expense")
    suspend fun deleteExpense(
        @Body expense: NetworkExpense
    ): Response<String>

    @GET("user/inventory/get-all-inventory")
    suspend fun getAllInventory(): Response<List<NetworkStockItem>>

    @POST("user/inventory/insert-inventory")
    suspend fun insertInventory(
        @Body stockItem: NetworkStockItem
    ): Response<String>

    @POST("user/inventory/update-stock")
    suspend fun updateInventory(
        @Body stockItem: NetworkStockItem
    ): Response<String>

    @POST("user/inventory/delete-inventory")
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