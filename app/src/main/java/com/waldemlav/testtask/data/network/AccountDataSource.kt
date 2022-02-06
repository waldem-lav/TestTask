package com.waldemlav.testtask.data.network

import com.waldemlav.testtask.data.network.model.ResponseDto
import com.waldemlav.testtask.data.network.model.SignUserDtoIn
import com.waldemlav.testtask.data.network.model.SignUserDtoOut
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class AccountDataSource @Inject constructor(
    private val accountApi: AccountApi
) {
    suspend fun signIn(userData: SignUserDtoIn): ResponseDto<SignUserDtoOut> {
        return accountApi.signIn(userData)
    }

    suspend fun signUp(userData: SignUserDtoIn): ResponseDto<SignUserDtoOut> {
        return accountApi.signUp(userData)
    }
}

interface AccountApi {
    @POST("account/signin")
    suspend fun signIn(@Body userData: SignUserDtoIn): ResponseDto<SignUserDtoOut>

    @POST("account/signup")
    suspend fun signUp(@Body userData: SignUserDtoIn): ResponseDto<SignUserDtoOut>
}