package com.rapsealk.coronamask.util

/**
 * Created by rapsealk on 2020/03/11..
 */
class Event<T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent(): T = content
}