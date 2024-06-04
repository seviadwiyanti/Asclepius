package com.dicoding.asclepius.data.local.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.database.RiwayatDao
import com.dicoding.asclepius.data.local.entity.RiwayatEntity
import com.dicoding.asclepius.data.local.database.RiwayatRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RiwayatRepository(application: Application) {
    private val mRiwayatDao: RiwayatDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = RiwayatRoomDatabase.getDatabase(application)
        mRiwayatDao = db.riwayatDao()
    }

    fun getAllRiwayat(): LiveData<List<RiwayatEntity>> = mRiwayatDao.getRiwayat()
    fun insert(result: RiwayatEntity) {
        executorService.execute { mRiwayatDao.insert(result) }
    }
}