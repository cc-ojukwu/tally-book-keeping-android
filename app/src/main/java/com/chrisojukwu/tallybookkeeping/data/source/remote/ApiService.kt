package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.models.SignInUser
import com.chrisojukwu.tallybookkeeping.data.models.Token
import com.chrisojukwu.tallybookkeeping.data.models.User
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


private const val BASE_URL = "http://10.0.2.2:8080"
//private const val BASE_URL = "https://c8f9dc74-ef82-4c2c-a831-edf3fe8c6148.mock.pstmn.io"
//private const val BASE_URL = "https://3c915b37-e37d-416f-b297-01f2c4e2bb98.mock.pstmn.io"

//use moshi library to parse json into kotlin classes
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()

private val gson = GsonBuilder()
    //.setLenient()
    .create()

interface ApiService {
    //create new user account
    @Headers("Content-Type: application/json")
    @POST("/process-register")
    suspend fun createNewUserAccount(
        @Body userInfo: User
    ): Response<String>

    @GET("/get")
    suspend fun getIt(): Response<String>

    //login user
    @POST("/auth/login")
    suspend fun signInUser(
        @Body userInfo: SignInUser
    ): Response<Token>

    //login google user
    @POST("/auth/oauth2/logintest")
    suspend fun googleSignIn(
        @Header("google_id_token") idToken: String
    ): Response<Token>


}

//use retrofit object to retrieve REST API response
object Api {
    val retrofitService: ApiService by lazy {
        Retrofit.Builder()
            //.addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }
}