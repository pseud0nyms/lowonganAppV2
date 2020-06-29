package com.example.lokerandroid.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class LowonganKerja(
    @ColumnInfo(name = "id_lowongan")
    @SerializedName("id_lowongan")
    val id_lowongan: String?,

    @ColumnInfo(name = "perusahaan")
    @SerializedName("perusahaan")
    val perusahaan: String?,

    @ColumnInfo(name = "posisi")
    @SerializedName("posisi")
    val posisi: String?,

    @ColumnInfo(name = "kategori")
    @SerializedName("kategori")
    val kategori: String?,

    @ColumnInfo(name = "cabang")
    @SerializedName("cabang")
    val cabang: String?,

    @ColumnInfo(name = "syarat")
    @SerializedName("syarat")
    val syarat: String?,

    @ColumnInfo(name = "gaji")
    @SerializedName("gaji")
    val gaji: String?,

    @ColumnInfo(name = "gambar")
    @SerializedName("gambar")
    val gambar: String?,

    @ColumnInfo(name = "deskripsi")
    @SerializedName("deskripsi")
    val deskripsi: String?,

    @ColumnInfo(name = "lokasi")
    @SerializedName("lokasi")
    val lokasi: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

data class LokerPalette(var color: Int)

data class SmsInfo(
    var to: String?,
    var text: String?,
    var imageUrl: String?
)