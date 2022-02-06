package com.waldemlav.testtask.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="comment")
data class CommentLocalDto(
    @PrimaryKey val id: Int,
    val imageId: Int,
    val date: Long,
    val text: String
)