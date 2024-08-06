package com.example.galleryapp.di

import com.example.galleryapp.data.ImagesRepositoryImpl
import com.example.galleryapp.domain.ImagesRepository
import org.koin.dsl.module

val repoModule = module {
    single<ImagesRepository> {ImagesRepositoryImpl(get())}

}