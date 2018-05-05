package br.com.rodrigohsb.challenge.viewModel

import android.arch.lifecycle.ViewModel
import br.com.rodrigohsb.challenge.*
import io.reactivex.Observable

/**
 * @rodrigohsb
 */
class MyViewModel: ViewModel() {

    private var pageCount = 1

    private val myService by lazy { MyService() }

    fun listPhotos(): Observable<State> {

        return myService
                .loadPhotos(pageCount)
                .map {
                    pageCount++
                    it
                }
                .map { MyTransformer().convert(it) }
                .compose(MyCompose())
                .startWith(State.Loading())
    }


    fun loadMore(): Observable<State> {

        return myService
                .loadPhotos(pageCount)
                .map {
                    pageCount++
                    it
                }
                .map { MyTransformer().convert(it) }
                .compose(MyCompose())
    }
}