package com.waldemlav.testtask.data.network.model

data class ResponseDto<T>(
    val status: Int,
    val data: T?
)