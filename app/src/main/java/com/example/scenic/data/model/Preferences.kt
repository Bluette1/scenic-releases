package com.example.scenic.data.model

import com.google.gson.annotations.SerializedName

data class Preferences(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val volume: Double,
    @SerializedName("selected_track")
    val selectedTrack: String?, /* JSON might allow string or number */
    @SerializedName("image_transition_interval")
    val imageTransitionInterval: Int
)

data class UserPreferencesResponse(
    val preferences: Preferences
)
