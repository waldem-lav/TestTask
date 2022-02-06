package com.waldemlav.testtask.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="photo")
data class PhotoLocalDto(
    @PrimaryKey val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)