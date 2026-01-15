package com.example.scenic.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scenic.data.local.entity.AudioEntity
import com.example.scenic.data.local.entity.ImageEntity

@Database(entities = [ImageEntity::class, AudioEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vibesDao(): VibesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "scenic_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
