package br.com.rodrigohsb.challenge

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @rodrigohsb
 */
interface MyWebServiceAPI {

    @GET("photos")
    fun loadPhotos(@Query("client_id") id: String,@Query("page") pageCount: Int): Observable<List<MyResponseObject>>

}