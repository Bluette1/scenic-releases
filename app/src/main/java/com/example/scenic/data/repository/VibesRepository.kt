package com.example.scenic.data.repository

import com.example.scenic.data.local.VibesDao
import com.example.scenic.data.local.entity.toEntity
import com.example.scenic.data.model.*
import com.example.scenic.data.network.RetrofitClient

class VibesRepository(private val dao: VibesDao) {
    private val api = RetrofitClient.apiService

    suspend fun getImages(): Result<List<Image>> {
        return try {
            val response = api.getImages()
            dao.clearImages()
            dao.insertImages(response.map { it.toEntity() })
            Result.success(response)
        } catch (e: Exception) {
            // Fallback to local
            val local = dao.getImages().map { it.toDomainModel() }
            if (local.isNotEmpty()) {
                Result.success(local)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getAudios(): Result<List<Audio>> {
        return try {
            val response = api.getAudios()
            dao.clearAudios()
            dao.insertAudios(response.map { it.toEntity() })
            Result.success(response)
        } catch (e: Exception) {
            val local = dao.getAudios().map { it.toDomainModel() }
            if (local.isNotEmpty()) {
                Result.success(local)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val body = mapOf("email" to email, "password" to password)
            val response = api.login(body)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<AuthResponse> {
        return try {
            val body = mapOf("email" to email, "password" to password, "name" to name)
            val response = api.register(body)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserPreferences(token: String): Result<Preferences> {
        return try {
            val response = api.getUserPreferences("Bearer $token")
            Result.success(response.preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveUserPreferences(token: String, preferences: Preferences): Result<Unit> {
        return try {
            // Wrapping the preferences object in a UserPreferencesResponse container for the API body
            val body = UserPreferencesResponse(preferences)
            api.saveUserPreferences("Bearer $token", body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

