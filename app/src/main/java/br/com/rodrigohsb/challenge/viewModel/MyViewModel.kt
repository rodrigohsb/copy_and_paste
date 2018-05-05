package br.com.rodrigohsb.challenge.viewModel

import android.arch.lifecycle.ViewModel
import br.com.rodrigohsb.challenge.*
import br.com.rodrigohsb.challenge.service.MyService
import io.reactivex.Observable

/**
 * @rodrigohsb
 */
class MyViewModel (val myService: MyService): ViewModel() {

    private var pageCount = 1

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