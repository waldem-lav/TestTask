package com.waldemlav.testtask.data.repository

import com.waldemlav.testtask.data.cache.database.LocalDao
import com.waldemlav.testtask.data.cache.mapper.CommentLocalDtoMapper
import com.waldemlav.testtask.data.cache.mapper.ImageLocalDtoMapper
import com.waldemlav.testtask.data.cache.model.CommentLocalDto
import com.waldemlav.testtask.data.cache.model.PhotoLocalDto
import com.waldemlav.testtask.data.network.*
import com.waldemlav.testtask.data.network.mapper.CommentDtoMapper
import com.waldemlav.testtask.data.network.mapper.ImageDtoMapper
import com.waldemlav.testtask.data.network.model.*
import com.waldemlav.testtask.domain.model.CommentData
import com.waldemlav.testtask.domain.model.PhotoData
import com.waldemlav.testtask.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val accountDataSource: AccountDataSource,
    private val imageDataSource: ImageDataSource,
    private val imageDtoMapper: ImageDtoMapper,
    private val localDao: LocalDao,
    private val commentDataSource: CommentDataSource,
    private val commentDtoMapper: CommentDtoMapper,
    private val imageLocalDtoMapper: ImageLocalDtoMapper,
    private val commentLocalDtoMapper: CommentLocalDtoMapper,
    private val tokenInterceptor: TokenInterceptor
) {

    suspend fun signIn(userData: SignUserDtoIn): Resource<SignUserDtoOut> {
        return try {
            val response = accountDataSource.signIn(userData)
            val result = response.data
            if (response.status == 200 && result != null)
                Resource.Success(result)
            else
                Resource.Error("HTTP " + response.status)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun signUp(userData: SignUserDtoIn): Resource<SignUserDtoOut> {
        return try {
            val response = accountDataSource.signUp(userData)
            val result = response.data
            if (response.status == 200 && result != null)
                Resource.Success(result)
            else
                Resource.Error("HTTP " + response.status)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun downloadImages(page: Int): Resource<List<PhotoLocalDto>> {
        return try {
            val response = imageDataSource.getImages(page)
            val result = response.data
            if (response.status == 200 && result != null)
                Resource.Success(imageDtoMapper.mapListToCacheModel(result))
            else
                Resource.Error("HTTP " + response.status)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun uploadImage(imageDtoIn: ImageDtoIn): Resource<PhotoLocalDto> {
        return try {
            val response = imageDataSource.uploadImage(imageDtoIn)
            val result = response.data
            if (response.status == 200 && result != null)
                Resource.Success(imageDtoMapper.mapToCacheModel(result))
            else
                Resource.Error("HTTP " + response.status)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun cacheImages(list: List<PhotoLocalDto>) {
        withContext(Dispatchers.IO) {
            localDao.insertPhotos(list)
        }
    }

    suspend fun cacheImage(photoLocalDto: PhotoLocalDto) {
        withContext(Dispatchers.IO) {
            localDao.insertPhoto(photoLocalDto)
        }
    }

    suspend fun getImages(): List<PhotoData> {
        return withContext(Dispatchers.IO) {
            imageLocalDtoMapper.mapListToDomainModel(localDao.getSavedPhotos())
        }
    }

    suspend fun deleteImage(id: Int): Boolean {
        return try {
            val response = imageDataSource.deleteImage(id)
            response.status == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteImageFromCache(photo: PhotoData) {
        withContext(Dispatchers.IO) {
            localDao.deletePhoto(imageLocalDtoMapper.mapFromDomainModel(photo))
        }
    }

    suspend fun downloadComments(imageId: Int, page: Int): Resource<List<CommentLocalDto>> {
        return try {
            val response = commentDataSource.getComments(imageId, page)
            val result = response.data
            if (response.status == 200 && result != null)
                Resource.Success(commentDtoMapper.mapListToCacheModel(result, imageId))
            else
                Resource.Error("HTTP " + response.status)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun uploadComment(commentDtoIn: CommentDtoIn, imageId: Int): Resource<CommentLocalDto> {
        return try {
            val response = commentDataSource.postComment(commentDtoIn, imageId)
            val result = response.data
            if (response.status == 200 && result != null)
                Resource.Success(commentDtoMapper.mapToCacheModel(result, imageId))
            else
                Resource.Error("HTTP " + response.status)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun cacheComments(list: List<CommentLocalDto>) {
        withContext(Dispatchers.IO) {
            localDao.insertComments(list)
        }
    }

    suspend fun cacheComment(commentLocalDto: CommentLocalDto) {
        withContext(Dispatchers.IO) {
            localDao.insertComment(commentLocalDto)
        }
    }

    suspend fun getSavedComments(imageId: Int): List<CommentData> {
        return withContext(Dispatchers.IO) {
            commentLocalDtoMapper.mapListToDomainModel(localDao.getSavedComments(imageId))
        }
    }

    suspend fun deleteComment(commentId: Int, imageId: Int): Boolean {
        return try {
            val response = commentDataSource.deleteComment(commentId, imageId)
            response.status == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteCommentFromCache(commentData: CommentData) {
        withContext(Dispatchers.IO) {
            localDao.deleteComment(commentLocalDtoMapper.mapFromDomainModel(commentData))
        }
    }

    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            localDao.deletePhotoCache()
            localDao.deleteCommentCache()
        }
    }

    suspend fun isCacheEmpty(): Boolean {
        return withContext(Dispatchers.IO) {
            localDao.getSavedCommentsCount() == 0
        }
    }

    fun setToken(token: String) {
        tokenInterceptor.token = token
    }
}