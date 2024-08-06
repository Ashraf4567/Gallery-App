package com.example.galleryapp.utls



sealed class DataState<T>(var data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): DataState<T>(data)
    class Error<T>(message: String, data: T? = null) : DataState<T>(data, message)
    class Loading<T>(data: T? = null) : DataState<T>(data)
}