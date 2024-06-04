package com.dicoding.asclepius.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.RiwayatEntity

@Dao
interface RiwayatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(result: RiwayatEntity)

    @Query("SELECT * FROM riwayat ORDER BY id ASC")
    fun getRiwayat(): LiveData<List<RiwayatEntity>>
}