package com.example.lokerandroid.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.lokerandroid.data.dao.LowonganDatabase
import com.example.lokerandroid.data.model.LowonganKerja
import com.example.lokerandroid.ui.base.BaseLokerViewModel
import kotlinx.coroutines.launch

class DetailLokerViewModel(application: Application): BaseLokerViewModel(application) {

    val lokerLiveData = MutableLiveData<LowonganKerja>()

    fun fetch(uuid: Int){
        launch {
            val loker = LowonganDatabase(
                getApplication()
            ).lowonganDao().getLowongan(uuid)
            lokerLiveData.value = loker
        }
    }
}