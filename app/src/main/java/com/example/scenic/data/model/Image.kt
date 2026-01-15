package com.example.scenic.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    val src: String,
    @SerializedName("description")
    val alt: String,
    val title: String? = null
)
