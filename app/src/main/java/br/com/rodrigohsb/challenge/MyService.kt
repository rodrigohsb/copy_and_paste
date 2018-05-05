package br.com.rodrigohsb.challenge

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @rodrigohsb
 */
class MyService {

    private val webServiceAPI by lazy{
        MyApplication.getInstance().getWebServiceAPI()
    }

    fun loadPhotos(pageCount: Int): Observable<List<MyResponseObject>> {
        return webServiceAPI.
                loadPhotos("a8861af9539a7fce15f9ca4cb443d62423000e32fb5db3377c7209e08f16ca3a", pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}