package com.example.galleryapp.data

import com.example.galleryapp.domain.Image

interface ImagesApiService {

    suspend fun getImages(): List<Image>
}