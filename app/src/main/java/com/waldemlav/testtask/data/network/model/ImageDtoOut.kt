package com.waldemlav.testtask.data.network.model

data class ImageDtoOut(
    val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)