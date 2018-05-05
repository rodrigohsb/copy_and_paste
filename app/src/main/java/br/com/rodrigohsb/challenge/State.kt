package br.com.rodrigohsb.challenge

import br.com.rodrigohsb.challenge.entry.Photo

/**
 * @rodrigohsb
 */
sealed class State {

    class Loading: State()

    class Error(): State()

    data class Success(val photos: List<Photo>): State()

}