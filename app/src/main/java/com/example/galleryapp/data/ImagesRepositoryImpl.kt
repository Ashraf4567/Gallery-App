package com.example.galleryapp.data

import com.example.galleryapp.R
import com.example.galleryapp.domain.Image
import com.example.galleryapp.domain.ImagesRepository
import com.example.galleryapp.utls.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImagesRepositoryImpl(
private val network: ImagesApiService
): ImagesRepository {
    override fun getImages(): Flow<DataState<List<Image>>> = flow {
        emit(DataState.Loading())
        try {
            val images = network.getImages()
            emit(DataState.Success(images))
        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Error("Something went wrong ${e.localizedMessage}"))
        }
    }

    override fun getResourcesImages(): Flow<DataState<List<Int>>> = flow {
        emit(DataState.Loading())
        try {
            val images = listOf(
                R.drawable.img_cat,
                R.drawable.happy,
                R.drawable.img_sea,
                R.drawable.neutral,
                R.drawable.img_tree,
                R.drawable.calm,
                R.drawable.happy
            )
            emit(DataState.Success(images))
        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Error("Something went wrong ${e.localizedMessage}"))
        }
    }
}