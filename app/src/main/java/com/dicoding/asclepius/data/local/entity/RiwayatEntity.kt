package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "riwayat")
@Parcelize
data class RiwayatEntity(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "score")
    var score: String,

    @ColumnInfo(name = "createdAt")
    var createdAt: String
) : Parcelable