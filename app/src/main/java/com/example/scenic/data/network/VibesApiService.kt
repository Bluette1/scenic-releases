package com.example.scenic.data.network

import com.example.scenic.data.model.*
import retrofit2.http.*

interface VibesApiService {
    @GET("api/images")
    suspend fun getImages(): List<Image>

    @GET("api/audios")
    suspend fun getAudios(): List<Audio>

    @POST("users/tokens/sign_up")
    suspend fun register(@Body body: Map<String, String>): AuthResponse

    @POST("users/tokens/sign_in")
    suspend fun login(@Body body: Map<String, String>): AuthResponse

    @GET("api/user_preferences")
    suspend fun getUserPreferences(@Header("Authorization") token: String): UserPreferencesResponse

    @POST("api/user_preferences")
    suspend fun saveUserPreferences(
        @Header("Authorization") token: String,
        @Body body: UserPreferencesResponse // Using reuse of response structure for request if it matches, else Map
    )
}
