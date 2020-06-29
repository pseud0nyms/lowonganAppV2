package com.example.lokerandroid.data.repository

import com.example.lokerandroid.data.api.LowonganApi
import com.example.lokerandroid.data.model.LowonganKerja
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class LowonganApiService {

    private  val BASE_URL = "https://lowonganapi.herokuapp.com"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(LowonganApi::class.java)

    fun getLowongan(): Single<List<LowonganKerja>> {
        return api.getLowongan()
    }
}