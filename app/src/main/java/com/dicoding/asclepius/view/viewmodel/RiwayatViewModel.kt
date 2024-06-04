package com.dicoding.asclepius.view.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.Repository.RiwayatRepository
import com.dicoding.asclepius.data.local.entity.RiwayatEntity

class RiwayatViewModel(application: Application) : ViewModel() {
    private val mRiwayatRepository: RiwayatRepository = RiwayatRepository(application)

    fun insert(result: RiwayatEntity) {
        mRiwayatRepository.insert(result)
    }

    fun getRiwayat(): LiveData<List<RiwayatEntity>> = mRiwayatRepository.getAllRiwayat()
}