package com.waldemlav.testtask.data.network

import com.waldemlav.testtask.data.network.model.*
import retrofit2.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageDataSource @Inject constructor(
    private val imageApi: ImageApi
) {
    suspend fun uploadImage(imageDtoIn: ImageDtoIn): ResponseDto<ImageDtoOut> {
        return imageApi.uploadImage(imageDtoIn)
    }

    suspend fun getImages(page: Int): ResponseDto<List<ImageDtoOut>> {
        return imageApi.getImages(page)
    }

    suspend fun deleteImage(id: Int): ResponseDto<ImageDtoOut> {
        return imageApi.deleteImage(id)
    }
}

interface ImageApi {
    @POST("/api/image")
    suspend fun uploadImage(@Body imageDtoIn: ImageDtoIn): ResponseDto<ImageDtoOut>

    @GET("/api/image")
    suspend fun getImages(@Query("page") page: Int): ResponseDto<List<ImageDtoOut>>

    @DELETE("/api/image/{id}")
    suspend fun deleteImage(@Path("id") id: Int): ResponseDto<ImageDtoOut>
}