package com.example.lokerandroid.data.api

import com.example.lokerandroid.data.model.LowonganKerja
import io.reactivex.Single
import retrofit2.http.GET

interface LowonganApi {
    @GET("/api/v1/lowongan")
    fun getLowongan(): Single<List<LowonganKerja>>
}