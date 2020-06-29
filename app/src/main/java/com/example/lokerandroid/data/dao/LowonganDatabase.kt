package com.example.lokerandroid.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lokerandroid.data.model.LowonganKerja

@Database(entities = arrayOf(LowonganKerja::class), version = 1)
abstract class LowonganDatabase: RoomDatabase() {
    abstract fun lowonganDao(): LowonganDao

    companion object {
        @Volatile private  var instance: LowonganDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
            instance
                ?: buildDatabase(
                    context
                ).also {
                instance = it
            }
        }

        private fun  buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            LowonganDatabase::class.java,
            "lowongandatabase"
        ).build()
    }
}