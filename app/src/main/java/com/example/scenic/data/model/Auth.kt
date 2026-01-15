package com.example.scenic.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String
)

data class AuthResponse(
    val token: String,
    @SerializedName("resource_owner")
    val user: User
)
