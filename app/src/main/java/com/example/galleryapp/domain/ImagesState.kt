package com.example.galleryapp.domain

data class ImagesState<T>(
    val images: List<T> = emptyList(),
    val isLoading: Boolean = true,
    val error: String = ""
)