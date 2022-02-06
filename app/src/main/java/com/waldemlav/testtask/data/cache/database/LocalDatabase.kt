package com.waldemlav.testtask.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waldemlav.testtask.data.cache.model.CommentLocalDto
import com.waldemlav.testtask.data.cache.model.PhotoLocalDto

@Database(entities = [PhotoLocalDto::class, CommentLocalDto::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun imageDao(): LocalDao

    companion object {
        const val DATABASE_NAME = "image_db"
    }
}