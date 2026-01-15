package com.example.scenic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.scenic.data.model.Audio

@Entity(tableName = "audios")
data class AudioEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val url: String
) {
    fun toDomainModel(): Audio = Audio(id = id, title = title, url = url)
}

fun Audio.toEntity(): AudioEntity = AudioEntity(id = id, title = title, url = url)
