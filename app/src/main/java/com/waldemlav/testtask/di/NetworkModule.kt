package com.waldemlav.testtask.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waldemlav.testtask.data.network.*
import com.waldemlav.testtask.data.network.mapper.CommentDtoMapper
import com.waldemlav.testtask.data.network.mapper.ImageDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshiBuilder(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("http://junior.balinasoft.com/api/")
    }

    @Singleton
    @Provides
    fun provideAccountApi(retrofit: Retrofit.Builder): AccountApi {
        return retrofit.build().create(AccountApi::class.java)
    }

    @Singleton
    fun provideAccountDataSource(accountApi: AccountApi): AccountDataSource {
        return AccountDataSource(accountApi)
    }

    @Singleton
    @Provides
    fun provideImageApi(retrofit: Retrofit.Builder): ImageApi {
        return retrofit.build().create(ImageApi::class.java)
    }

    @Singleton
    fun provideImageDataSource(imageApi: ImageApi): ImageDataSource {
        return ImageDataSource(imageApi)
    }

    @Singleton
    fun provideImageDtoMapper(): ImageDtoMapper {
        return ImageDtoMapper()
    }

    @Singleton
    @Provides
    fun provideCommentApi(retrofit: Retrofit.Builder): CommentApi {
        return retrofit.build().create(CommentApi::class.java)
    }

    @Singleton
    fun provideCommentDataSource(commentApi: CommentApi): CommentDataSource {
        return CommentDataSource(commentApi)
    }

    @Singleton
    fun provideCommentDtoMapper(): CommentDtoMapper {
        return CommentDtoMapper()
    }
}