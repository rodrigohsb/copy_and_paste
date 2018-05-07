package br.com.rodrigohsb.challenge.di

import android.content.Context
import android.net.ConnectivityManager
import br.com.rodrigohsb.challenge.service.MyService
import br.com.rodrigohsb.challenge.viewModel.MyViewModel
import br.com.rodrigohsb.challenge.webservice.MyWebServiceAPI
import br.com.rodrigohsb.challenge.webservice.NetworkService
import br.com.rodrigohsb.challenge.webservice.interceptor.RequestInterceptor
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @rodrigohsb
 */
class Injector (val context: Context) {

    val dependencies = Kodein.Module(allowSilentOverride = true) {

        bind<MyViewModel>() with provider{
            MyViewModel(myService = instance())
        }

        bind<MyService>() with provider {
            MyService(webServiceAPI = instance())
        }

        bind<NetworkService>() with provider {
            NetworkService(connectivityManager = instance())
        }

        bind<ConnectivityManager>() with provider {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        bind<OkHttpClient>() with provider {
            val builder = OkHttpClient.Builder()
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.addInterceptor(instance(REQUEST_INTERCEPTOR))
            builder.addNetworkInterceptor(instance(LOG_INTERCEPTOR))
            builder.build()
        }

        bind<Interceptor>(tag = LOG_INTERCEPTOR) with provider {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            logging
        }

        bind<Interceptor>(tag = REQUEST_INTERCEPTOR) with provider {
            RequestInterceptor(networkService = instance())
        }

        bind<CallAdapter.Factory>() with provider {
            RxJava2CallAdapterFactory.create()
        }

        bind<Converter.Factory>() with provider {
            GsonConverterFactory.create()
        }

        bind<Retrofit>() with provider {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(instance())
                    .addConverterFactory(instance())
                    .client(instance())
                    .baseUrl("https://api.unsplash.com/")

            retrofit.build()
        }

        bind<MyWebServiceAPI>() with provider {
            instance<Retrofit>().create(MyWebServiceAPI::class.java)
        }
    }

    companion object {
        val LOG_INTERCEPTOR = "LogInterceptor"
        val REQUEST_INTERCEPTOR = "RequestInterceptor"
    }

}