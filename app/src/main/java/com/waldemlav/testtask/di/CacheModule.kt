package com.waldemlav.testtask.di

import android.content.Context
import androidx.room.Room
import com.waldemlav.testtask.data.cache.database.LocalDao
import com.waldemlav.testtask.data.cache.database.LocalDatabase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Provides

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideImageDb(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            LocalDatabase.DATABASE_NAME
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideImageDao(localDatabase: LocalDatabase): LocalDao {
        return localDatabase.imageDao()
    }
}