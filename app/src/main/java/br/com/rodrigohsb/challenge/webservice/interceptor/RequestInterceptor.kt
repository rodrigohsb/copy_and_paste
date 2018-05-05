package br.com.rodrigohsb.challenge.webservice.interceptor

import br.com.rodrigohsb.challenge.webservice.NetworkService
import br.com.rodrigohsb.challenge.webservice.exceptions.*
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * @rodrigohsb
 */
class RequestInterceptor (val networkService: NetworkService): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if(isNotConnected()) throw NoNetworkException()

        return try{

            val request = chain.request()
            val response = chain.proceed(request)

            when(response.code()){
                400 -> throw BadRequestException()
                404 -> throw NoDataException()
                401,402,403, in 405..499 -> throw Error4XXException()
                in 500..599 -> throw Error5XXException()
                else -> response
            }

        }catch (e: Exception){
            when(e) {
                is IOException, is SocketException -> throw TimeoutException()
                else -> throw e
            }
        }

    }

    private fun isNotConnected() = !networkService.isConnected
}