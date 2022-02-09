package com.waldemlav.testtask.data.cache.mapper

import com.waldemlav.testtask.data.cache.model.CommentLocalDto
import com.waldemlav.testtask.domain.model.CommentData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentLocalDtoMapper @Inject constructor() {

    private fun mapToDomainModel(cacheModel: CommentLocalDto): CommentData {
        return CommentData(
            id = cacheModel.id,
            imageId = cacheModel.imageId,
            date = cacheModel.date,
            text = cacheModel.text
        )
    }

    fun mapListToDomainModel(cacheModelList: List<CommentLocalDto>): List<CommentData> {
        return cacheModelList.map { mapToDomainModel(it) }
    }

    fun mapFromDomainModel(domainModel: CommentData): CommentLocalDto {
        return CommentLocalDto(
            id = domainModel.id,
            imageId = domainModel.imageId,
            date = domainModel.date,
            text = domainModel.text
        )
    }
}