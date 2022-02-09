package com.waldemlav.testtask.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waldemlav.testtask.data.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun provideOkHttpClient(interceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("http://junior.balinasoft.com/api/")
            .client(client)
    }

    @Singleton
    @Provides
    fun provideAccountApi(retrofit: Retrofit.Builder): AccountApi {
        return retrofit.build().create(AccountApi::class.java)
    }

    @Singleton
    @Provides
    fun provideImageApi(retrofit: Retrofit.Builder): ImageApi {
        return retrofit.build().create(ImageApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCommentApi(retrofit: Retrofit.Builder): CommentApi {
        return retrofit.build().create(CommentApi::class.java)
    }
}