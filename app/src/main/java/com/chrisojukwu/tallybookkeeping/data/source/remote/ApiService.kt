package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkInventoryItem
import com.chrisojukwu.tallybookkeeping.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/change-password")
    suspend fun changePassword(
        @Body oldNewPassword: OldNewPassword
    ): Response<StringResponse>

    @POST("auth/forgot-password")
    suspend fun resetPassword(
        @Body resetPasswordEmail: ResetPasswordEmail
    ): Response<StringResponse>

    @POST("auth/change-email")
    suspend fun changeEmail(
        @Body user: User
    ): Response<TokenWithEmail>

    @POST("auth/email-create-account")
    suspend fun createNewUserAccount(
        @Body userInfo: User
    ): Response<StringResponse>

    @POST("auth/email-sign-in")
    suspend fun signInWithEmail(
        @Body userInfo: SignInUser
    ): Response<Token>

    @POST("auth/update-user-info")
    suspend fun updateUserInfo(
        @Body userInfo: User
    ): Response<User>

    @GET("auth/get-user-info")
    suspend fun getUserInfo(): Response<User>

    @POST("auth/oauth2/google-sign-in")
    suspend fun googleSignIn(
        @Header("google_id_token") idToken: String
    ): Response<TokenWithEmail>

    @GET("user/income/get-all-income")
    suspend fun getAllIncome(): Response<List<NetworkIncome>>

    @POST("user/income/create-income")
    suspend fun insertIncome(
        @Body income: NetworkIncome
    ): Response<StringResponse>

    @POST("user/income/update-income")
    suspend fun updateIncome(
        @Body income: NetworkIncome
    ): Response<StringResponse>

    @POST("user/income/delete-income")
    suspend fun deleteIncome(
        @Body income: NetworkIncome
    ): Response<StringResponse>

    @GET("user/expense/get-all-expense")
    suspend fun getAllExpense(): Response<List<NetworkExpense>>

    @POST("user/expense/create-expense")
    suspend fun insertExpense(
        @Body expense: NetworkExpense
    ): Response<StringResponse>

    @POST("user/expense/update-expense")
    suspend fun updateExpense(
        @Body expense: NetworkExpense
    ): Response<StringResponse>

    @POST("user/expense/delete-expense")
    suspend fun deleteExpense(
        @Body expense: NetworkExpense
    ): Response<StringResponse>

    @GET("user/inventory/get-all-stock")
    suspend fun getAllInventory(): Response<List<NetworkInventoryItem>>

    @POST("user/inventory/create-stock")
    suspend fun saveInventoryItem(
        @Body stockItem: NetworkInventoryItem
    ): Response<StringResponse>

    @POST("user/inventory/update-stock")
    suspend fun updateInventoryItem(
        @Body stockItem: NetworkInventoryItem
    ): Response<StringResponse>

    @POST("user/inventory/delete-stock")
    suspend fun deleteInventoryItem(
        @Body stockItem: NetworkInventoryItem
    ): Response<StringResponse>

}
