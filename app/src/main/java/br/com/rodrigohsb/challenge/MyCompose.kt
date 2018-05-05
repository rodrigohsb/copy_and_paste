package br.com.rodrigohsb.challenge

import br.com.rodrigohsb.challenge.entry.Photo
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * @rodrigohsb
 */
class MyCompose: ObservableTransformer<List<Photo>,State> {

    override fun apply(upstream: Observable<List<Photo>>): ObservableSource<State> {

        return upstream.
                map {
                    photos: List<Photo> ->
                    State.Success(photos) as State
                }
                .onErrorResumeNext { error: Throwable ->
//                    Observable.just(State.Error(error as Exception))
                    Observable.just(State.Error())
                }
    }
}