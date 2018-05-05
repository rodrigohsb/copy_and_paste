package br.com.rodrigohsb.challenge

/**
 * @rodrigohsb
 */
sealed class State {

    class Loading: State()

    class Error(): State()

    data class Success(val photos: List<Photo>): State()

}