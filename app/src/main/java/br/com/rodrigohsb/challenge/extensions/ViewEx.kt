package br.com.rodrigohsb.challenge.extensions

import android.view.View

/**
 * @rodrigohsb
 */

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isGone() = this.visibility == View.GONE

fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.gone(){
    this.visibility = View.GONE
}