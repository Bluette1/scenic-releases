package com.example.scenic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scenic.data.local.entity.AudioEntity
import com.example.scenic.data.local.entity.ImageEntity

@Dao
interface VibesDao {
    @Query("SELECT * FROM images")
    suspend fun getImages(): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Query("DELETE FROM images")
    suspend fun clearImages()

    @Query("SELECT * FROM audios")
    suspend fun getAudios(): List<AudioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudios(audios: List<AudioEntity>)

    @Query("DELETE FROM audios")
    suspend fun clearAudios()
}
