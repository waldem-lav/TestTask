package com.waldemlav.testtask.data.network.mapper

import com.waldemlav.testtask.data.cache.model.PhotoLocalDto
import com.waldemlav.testtask.data.network.model.ImageDtoOut
import javax.inject.Inject

class ImageDtoMapper
@Inject
constructor() {

    fun mapToCacheModel(dtoModel: ImageDtoOut): PhotoLocalDto {
        return PhotoLocalDto(
            id = dtoModel.id,
            url = dtoModel.url,
            date = dtoModel.date,
            lat = dtoModel.lat,
            lng = dtoModel.lng
        )
    }

    fun mapListToCacheModel(dtoModelList: List<ImageDtoOut>): List<PhotoLocalDto> {
        return dtoModelList.map { mapToCacheModel(it) }
    }
}