package com.example.galleryapp.di

import com.example.galleryapp.data.ImagesApiImpl
import com.example.galleryapp.data.ImagesApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val apiModule = module {
    factory<ImagesApiService>{ImagesApiImpl(get())}

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }
    }
}