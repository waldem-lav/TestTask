package com.waldemlav.testtask.data.cache.mapper

import com.waldemlav.testtask.data.cache.model.PhotoLocalDto
import com.waldemlav.testtask.domain.model.PhotoData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLocalDtoMapper @Inject constructor() {

    private fun mapToDomainModel(cacheModel: PhotoLocalDto): PhotoData {
        return PhotoData(
            id = cacheModel.id,
            url = cacheModel.url,
            date = cacheModel.date,
            lat = cacheModel.lat,
            lng = cacheModel.lng
        )
    }

    fun mapListToDomainModel(cacheModelList: List<PhotoLocalDto>): List<PhotoData> {
        return cacheModelList.map { mapToDomainModel(it) }
    }

    fun mapFromDomainModel(domainModel: PhotoData): PhotoLocalDto {
        return PhotoLocalDto(
            id = domainModel.id,
            url = domainModel.url,
            date = domainModel.date,
            lat = domainModel.lat,
            lng = domainModel.lng
        )
    }
}