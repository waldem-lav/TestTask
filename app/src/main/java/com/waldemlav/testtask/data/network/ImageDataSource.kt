package com.waldemlav.testtask.data.network

import com.waldemlav.testtask.data.network.model.*
import retrofit2.http.*
import javax.inject.Inject

class ImageDataSource @Inject constructor(
    private val imageApi: ImageApi
) {
    suspend fun uploadImage(token: String, imageDtoIn: ImageDtoIn): ResponseDto<ImageDtoOut> {
        return imageApi.uploadImage(token, imageDtoIn)
    }

    suspend fun getImages(token: String, page: Int): ResponseDto<List<ImageDtoOut>> {
        return imageApi.getImages(token, page)
    }

    suspend fun deleteImage(token: String, id: Int): ResponseDto<ImageDtoOut> {
        return imageApi.deleteImage(token, id)
    }
}

interface ImageApi {
    @POST("/api/image")
    suspend fun uploadImage(
        @Header("Access-Token") token: String,
        @Body imageDtoIn: ImageDtoIn
    ): ResponseDto<ImageDtoOut>

    @GET("/api/image")
    suspend fun getImages(
        @Header("Access-Token") token: String,
        @Query("page") page: Int
    ): ResponseDto<List<ImageDtoOut>>

    @DELETE("/api/image/{id}")
    suspend fun deleteImage(
        @Header("Access-Token") token: String,
        @Path("id") id: Int
    ): ResponseDto<ImageDtoOut>
}