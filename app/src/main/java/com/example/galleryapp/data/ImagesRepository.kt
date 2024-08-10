package com.example.galleryapp.data

import com.example.galleryapp.R
import com.example.galleryapp.data.model.Image
import com.example.galleryapp.utls.DataState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

object ImagesRepository{

    private val apiService = HttpClient(Android) {
        install(ContentNegotiation) {
            json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }
    }

    /**
     * Function to fetch images from the remote API.
     * Emits a loading state, followed by success or error depending on the result.
     * using flow to emit multiple values over a period of time
     */
    fun getImages(): Flow<DataState<List<Image>>> = flow {
        emit(DataState.Loading())
        try {
            val images: List<Image> = apiService.get("https://picsum.photos/v2/list").body()
            emit(DataState.Success(images))
        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Error("Something went wrong ${e.localizedMessage}"))
        }
    }


    // Function to fetch images from the local resources.
     fun getResourcesImages(): Flow<DataState<List<Int>>> = flow {
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