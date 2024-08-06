package com.example.galleryapp.domain

import com.example.galleryapp.utls.DataState
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    fun getImages(): Flow<DataState<List<Image>>>

    fun getResourcesImages(): Flow<DataState<List<Int>>>
}