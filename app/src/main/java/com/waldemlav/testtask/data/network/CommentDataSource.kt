package com.waldemlav.testtask.data.network

import com.waldemlav.testtask.data.network.model.*
import retrofit2.http.*
import javax.inject.Inject

class CommentDataSource @Inject constructor(
    private val commentApi: CommentApi
) {
    suspend fun getComments(
        token: String, imageId: Int, page: Int): ResponseDto<List<CommentDtoOut>> {

        return commentApi.getComments(token, imageId, page)
    }

    suspend fun deleteComment(
        token: String, commentId: Int, imageId: Int): ResponseDto<CommentDtoOut> {

        return commentApi.deleteComment(token, commentId, imageId)
    }

    suspend fun postComment(
        token: String, commentDtoIn: CommentDtoIn, imageId: Int): ResponseDto<CommentDtoOut> {

        return commentApi.postComment(token, commentDtoIn, imageId)
    }
}

interface CommentApi {
    @GET("/api/image/{imageId}/comment")
    suspend fun getComments(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): ResponseDto<List<CommentDtoOut>>

    @DELETE("/api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Access-Token") token: String,
        @Path("commentId") commentId: Int,
        @Path("imageId") imageId: Int
    ): ResponseDto<CommentDtoOut>

    @POST("/api/image/{imageId}/comment")
    suspend fun postComment(
        @Header("Access-Token") token: String,
        @Body commentDtoIn: CommentDtoIn,
        @Path("imageId") imageId: Int
    ): ResponseDto<CommentDtoOut>
}