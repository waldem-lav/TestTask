package com.waldemlav.testtask.ui.viewmodel

import androidx.lifecycle.*
import com.waldemlav.testtask.data.cache.model.CommentLocalDto
import com.waldemlav.testtask.data.cache.model.PhotoLocalDto
import com.waldemlav.testtask.data.network.model.CommentDtoIn
import com.waldemlav.testtask.data.network.model.ImageDtoIn
import com.waldemlav.testtask.data.network.model.SignUserDtoIn
import com.waldemlav.testtask.data.repository.MainRepository
import com.waldemlav.testtask.domain.model.CommentData
import com.waldemlav.testtask.domain.model.PhotoData
import com.waldemlav.testtask.util.Event
import com.waldemlav.testtask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private val _isUserLoggedIn = MutableLiveData(Event(false))
    val isUserLoggedIn: LiveData<Event<Boolean>> = _isUserLoggedIn

    lateinit var login: String
    var isCacheAvailable = false
    var isCacheEmpty = false

    private val _photos = MutableLiveData<List<PhotoData>>()
    val photos: LiveData<List<PhotoData>> = _photos

    private val _comments = MutableLiveData<List<CommentData>>()
    val comments: LiveData<List<CommentData>> = _comments

    fun signIn(userData: SignUserDtoIn) {
        viewModelScope.launch {
            when (val accountResponse = repository.signIn(userData)) {
                is Resource.Error ->
                    _errorMessage.value = Event(accountResponse.message.toString())
                is Resource.Success -> {
                    login = accountResponse.data!!.login
                    _isUserLoggedIn.value = Event(true)
                    repository.setToken(accountResponse.data.token)
                }
            }
        }
    }
    fun signUp(userData: SignUserDtoIn) {
        viewModelScope.launch {
            when (val accountResponse = repository.signUp(userData)) {
                is Resource.Error ->
                    _errorMessage.value = Event(accountResponse.message.toString())
                is Resource.Success -> {
                    login = accountResponse.data!!.login
                    _isUserLoggedIn.value = Event(true)
                    repository.setToken(accountResponse.data.token)
                }
            }
        }
    }

    fun isLoginValid(password: String): Boolean =
        Regex("[a-z0-9_\\-.@]{4,32}").matches(password)

    fun isPasswordValid(password: String): Boolean = password.length in 8..500

    fun uploadImage(imageDtoIn: ImageDtoIn) {
        viewModelScope.launch {
            when (val imageResponse = repository.uploadImage(imageDtoIn)) {
                is Resource.Error -> _errorMessage.value = Event(
                    "Uploading error: " + imageResponse.message)
                is Resource.Success -> {
                    repository.cacheImage(imageResponse.data!!)
                    getPhotoList()
                }
            }
        }
    }

    fun deleteImage(photoData: PhotoData) {
        viewModelScope.launch {
            if (repository.deleteImage(photoData.id)) {
                repository.deleteImageFromCache(photoData)
                getPhotoList()
            } else
                _errorMessage.value = Event("Deleting error")
        }
    }

    fun uploadComment(commentDtoIn: CommentDtoIn, imageId: Int) {
        viewModelScope.launch {
            when (val commentResponse = repository.uploadComment(commentDtoIn, imageId)){
                is Resource.Error -> _errorMessage.value = Event(
                    "Uploading error: " + commentResponse.message)
                is Resource.Success -> {
                    repository.cacheComment(commentResponse.data!!)
                    getCommentList(imageId)
                }
            }
        }
    }

    fun deleteComment(commentData: CommentData) {
        viewModelScope.launch {
            if (repository.deleteComment(commentData.id, commentData.imageId)) {
                repository.deleteCommentFromCache(commentData)
                getCommentList(commentData.imageId)
            } else
                _errorMessage.value = Event("Deleting error")
        }
    }

    fun getPhotoList() {
        viewModelScope.launch {
            _photos.value = repository.getImages()
        }
    }

    fun getCommentList(imageId: Int) {
        viewModelScope.launch {
            _comments.value = repository.getSavedComments(imageId)
        }
    }

    private var cacheJob: Job? = null
    fun cacheData() {
        cacheJob?.cancel()
        cacheJob = viewModelScope.launch {
            repository.clearCache()
            val cachePhotosList = mutableListOf<PhotoLocalDto>()
            var page = 0
            while (true) {
                when (val imagesResponse = repository.downloadImages(page)) {
                    is Resource.Success -> {
                        if (imagesResponse.data.isNullOrEmpty())
                            break
                        cachePhotosList.addAll(imagesResponse.data)
                        page++
                    }
                    is Resource.Error -> {
                        _errorMessage.value = Event(
                            "Caching error: " + imagesResponse.message
                        )
                        break
                    }
                }
            }
            repository.cacheImages(cachePhotosList)
            cacheComments(cachePhotosList)
            getPhotoList()
        }
    }

    private fun cacheComments(list: List<PhotoLocalDto>) {
        viewModelScope.launch {
            val cacheCommentList = mutableListOf<CommentLocalDto>()
            loop@ for (photo in list) {
                var page = 0
                while (true) {
                    when (val commentResponse = repository.downloadComments(photo.id, page)) {
                        is Resource.Success -> {
                            if (commentResponse.data.isNullOrEmpty())
                                break
                            cacheCommentList.addAll(commentResponse.data)
                            page++
                        }
                        is Resource.Error -> {
                            _errorMessage.value = Event(
                                "Caching error: " + commentResponse.message)
                            break@loop
                        }
                    }
                }
            }
            repository.cacheComments(cacheCommentList)
        }
    }

    fun checkIfCacheIsEmpty() {
        viewModelScope.launch {
            isCacheEmpty = repository.isCacheEmpty()
        }
    }
}