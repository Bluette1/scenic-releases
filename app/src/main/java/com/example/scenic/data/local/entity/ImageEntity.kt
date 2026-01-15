package com.example.scenic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.scenic.data.model.Image

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val src: String,
    val alt: String,
    val title: String? = null
) {
    fun toDomainModel(): Image = Image(src = src, alt = alt, title = title)
}

fun Image.toEntity(): ImageEntity = ImageEntity(src = src, alt = alt, title = title)
