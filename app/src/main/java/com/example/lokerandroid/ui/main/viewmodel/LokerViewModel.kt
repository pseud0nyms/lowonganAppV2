package com.example.lokerandroid.ui.main.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.lokerandroid.data.repository.LowonganApiService
import com.example.lokerandroid.data.dao.LowonganDatabase
import com.example.lokerandroid.data.model.LowonganKerja
import com.example.lokerandroid.ui.base.BaseLokerViewModel
import com.example.lokerandroid.utils.NotificationHelper
import com.example.lokerandroid.utils.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class LokerViewModel(application: Application): BaseLokerViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val lowonganService =
        LowonganApiService()
    private val disposable = CompositeDisposable()

    val loker = MutableLiveData<List<LowonganKerja>>()
    val lokerLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePrefenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePrefenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val loker = LowonganDatabase(
                getApplication()
            ).lowonganDao().getAllLowongan()
            lokerRetrieved(loker)
            Toast.makeText(getApplication(), "Loker retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
            lowonganService.getLowongan()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<LowonganKerja>>(){

                    override fun onSuccess(lokerList: List<LowonganKerja>) {
                        storeLokerLocally(lokerList)
                        Toast.makeText(getApplication(), "Loker retrieved from endpoint", Toast.LENGTH_SHORT).show()
                        NotificationHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        lokerLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun lokerRetrieved(lokerList: List<LowonganKerja>) {
        loker.value = lokerList
        lokerLoadError.value = false
        loading.value = false
    }

    private fun storeLokerLocally(list: List<LowonganKerja>) {
        launch {
            val dao = LowonganDatabase(
                getApplication()
            ).lowonganDao()
            dao.deleteAllLowongan()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            lokerRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}