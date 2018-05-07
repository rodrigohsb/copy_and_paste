package br.com.rodrigohsb.joao_challenge

import okhttp3.Interceptor

/**
 * @rodrigohsb
 */
class RequestInterceptorMock constructor(private val exception: Exception): Interceptor {
    override fun intercept(chain: Interceptor.Chain) = throw exception
}