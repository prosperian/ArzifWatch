package com.dip.arzifwatch.api

sealed class Resource<T> constructor(
    val data: T? = null,
    val message: String? = null,
) {

    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()

}