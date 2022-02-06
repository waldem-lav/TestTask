package com.waldemlav.testtask.data.network.model

data class ImageDtoIn(
    val base64Image: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)