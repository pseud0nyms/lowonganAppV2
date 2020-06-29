package com.example.lokerandroid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lokerandroid.data.model.LowonganKerja

@Dao
interface LowonganDao {
    @Insert
    suspend fun insertAll(vararg loker: LowonganKerja): List<Long>

    @Query("SELECT * FROM lowongankerja")
    suspend fun  getAllLowongan(): List<LowonganKerja>

    @Query("SELECT * FROM lowongankerja WHERE uuid = :id_lowongan")
    suspend fun getLowongan(id_lowongan: Int): LowonganKerja

    @Query("DELETE FROM lowongankerja")
    suspend fun deleteAllLowongan()
}