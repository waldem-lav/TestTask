package com.waldemlav.testtask.data.network.model

data class SignUserDtoOut(
    val userId: Int,
    val login: String,
    val token: String
)