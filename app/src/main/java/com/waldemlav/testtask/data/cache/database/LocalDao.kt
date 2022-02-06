package com.waldemlav.testtask.data.cache.database

import androidx.room.*
import com.waldemlav.testtask.data.cache.model.CommentLocalDto
import com.waldemlav.testtask.data.cache.model.PhotoLocalDto

@Dao
interface LocalDao {
    @Query("SELECT * FROM photo ORDER BY id DESC")
    fun getSavedPhotos(): List<PhotoLocalDto>

    @Query("SELECT * FROM comment WHERE imageId=:imageId ORDER BY id DESC")
    fun getSavedComments(imageId: Int): List<CommentLocalDto>

    @Query("SELECT COUNT(*) FROM comment")
    fun getSavedCommentsCount(): Int

    @Insert
    suspend fun insertPhoto(photo: PhotoLocalDto)

    @Insert
    suspend fun insertPhotos(photoList: List<PhotoLocalDto>)

    @Delete
    suspend fun deletePhoto(photo: PhotoLocalDto)

    @Insert
    suspend fun insertComment(comment: CommentLocalDto)

    @Insert
    suspend fun insertComments(commentList: List<CommentLocalDto>)

    @Delete
    suspend fun deleteComment(comment: CommentLocalDto)

    @Query("DELETE FROM photo")
    suspend fun deletePhotoCache()

    @Query("DELETE FROM comment")
    suspend fun deleteCommentCache()
}