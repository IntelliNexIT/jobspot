package com.intellinex.jobspot.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AuthInterceptor(private val token: String): Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return  chain.proceed(request)
    }
}