package com.waldemlav.testtask.data.network.mapper

import com.waldemlav.testtask.data.cache.model.CommentLocalDto
import com.waldemlav.testtask.data.network.model.CommentDtoOut
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentDtoMapper @Inject constructor() {

    fun mapToCacheModel(dtoModel: CommentDtoOut, imageId: Int): CommentLocalDto {
        return CommentLocalDto(
            id = dtoModel.id,
            imageId = imageId,
            date = dtoModel.date,
            text = dtoModel.text
        )
    }

    fun mapListToCacheModel(dtoModelList: List<CommentDtoOut>, imageId: Int): List<CommentLocalDto> {
        return dtoModelList.map { mapToCacheModel(it, imageId) }
    }
}