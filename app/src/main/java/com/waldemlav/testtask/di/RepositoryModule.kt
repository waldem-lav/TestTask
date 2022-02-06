package com.waldemlav.testtask.di

import com.waldemlav.testtask.data.cache.database.LocalDao
import com.waldemlav.testtask.data.cache.mapper.CommentLocalDtoMapper
import com.waldemlav.testtask.data.cache.mapper.ImageLocalDtoMapper
import com.waldemlav.testtask.data.network.AccountDataSource
import com.waldemlav.testtask.data.network.CommentDataSource
import com.waldemlav.testtask.data.network.ImageDataSource
import com.waldemlav.testtask.data.network.mapper.CommentDtoMapper
import com.waldemlav.testtask.data.network.mapper.ImageDtoMapper
import com.waldemlav.testtask.data.repository.MainRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    fun provideMainRepository(
        accountDataSource: AccountDataSource,
        imageDataSource: ImageDataSource,
        imageDtoMapper: ImageDtoMapper,
        localDao: LocalDao,
        commentDataSource: CommentDataSource,
        commentDtoMapper: CommentDtoMapper,
        imageLocalDtoMapper: ImageLocalDtoMapper,
        commentLocalDtoMapper: CommentLocalDtoMapper
    ): MainRepository {
        return MainRepository(
            accountDataSource,
            imageDataSource,
            imageDtoMapper,
            localDao,
            commentDataSource,
            commentDtoMapper,
            imageLocalDtoMapper,
            commentLocalDtoMapper
        )
    }
}