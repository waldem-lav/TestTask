@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.waldemlav.testtask.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor
@Inject constructor() : Interceptor {
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (request.header("Access-Token") != token && token != null)
            requestBuilder.header("Access-Token", token)

        return chain.proceed(requestBuilder.build())
    }
}