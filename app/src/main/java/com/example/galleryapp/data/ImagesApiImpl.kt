package com.example.galleryapp.data

import com.example.galleryapp.domain.Image
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class ImagesApiImpl(
    private val httpClient: HttpClient
): ImagesApiService {
    override suspend fun getImages(): List<Image> {
        return httpClient.get {
            url("https://picsum.photos/v2/list?page=2&limit=10")

        }.body()
    }
}