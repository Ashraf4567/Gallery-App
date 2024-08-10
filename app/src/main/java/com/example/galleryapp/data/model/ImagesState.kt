package com.example.galleryapp.data.model

data class ImagesState<T>(
    val images: List<T> = emptyList(),
    val isLoading: Boolean = true,
    val error: String = ""
)