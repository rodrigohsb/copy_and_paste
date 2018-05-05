package br.com.rodrigohsb.challenge

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @rodrigohsb
 */
class MyApplication: Application() {

    private lateinit var retrofit: Retrofit

    private var mWebServiceAPi: MyWebServiceAPI? = null

    companion object {

        private lateinit var mInstance: MyApplication

        fun getInstance(): MyApplication {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        createRetrofitInstance()
    }

    private fun createRetrofitInstance() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.readTimeout(5, TimeUnit.SECONDS)

        retrofit = Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun getWebServiceAPI(): MyWebServiceAPI {
        if (mWebServiceAPi == null) {
            mWebServiceAPi = retrofit.create<MyWebServiceAPI>(MyWebServiceAPI::class.java)
        }
        return mWebServiceAPi!!
    }

}