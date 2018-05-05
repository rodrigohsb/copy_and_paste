package br.com.rodrigohsb.challenge

import br.com.rodrigohsb.challenge.entry.Photo

/**
 * @rodrigohsb
 */
sealed class State {

    class Loading: State()

    data class Error(val exception: Exception): State()

    data class Success(val photos: List<Photo>): State()

}