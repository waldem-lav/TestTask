package com.waldemlav.testtask.data.network

import com.waldemlav.testtask.data.network.model.*
import retrofit2.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentDataSource @Inject constructor(
    private val commentApi: CommentApi
) {
    suspend fun getComments(imageId: Int, page: Int): ResponseDto<List<CommentDtoOut>> {
        return commentApi.getComments(imageId, page)
    }

    suspend fun deleteComment(commentId: Int, imageId: Int): ResponseDto<CommentDtoOut> {
        return commentApi.deleteComment(commentId, imageId)
    }

    suspend fun postComment(commentDtoIn: CommentDtoIn, imageId: Int): ResponseDto<CommentDtoOut> {
        return commentApi.postComment(commentDtoIn, imageId)
    }
}

interface CommentApi {
    @GET("/api/image/{imageId}/comment")
    suspend fun getComments(
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): ResponseDto<List<CommentDtoOut>>

    @DELETE("/api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Int,
        @Path("imageId") imageId: Int
    ): ResponseDto<CommentDtoOut>

    @POST("/api/image/{imageId}/comment")
    suspend fun postComment(
        @Body commentDtoIn: CommentDtoIn,
        @Path("imageId") imageId: Int
    ): ResponseDto<CommentDtoOut>
}