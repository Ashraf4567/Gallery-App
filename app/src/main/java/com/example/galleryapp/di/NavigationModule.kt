package com.example.galleryapp.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.galleryapp.presentation.screens.localImages.LocalImagesStoreFactory
import com.example.galleryapp.presentation.screens.onlineImages.OnlineImagesStoreFactory
import com.example.galleryapp.presentation.screens.resourceImages.ResourcesImagesFactory
import org.koin.dsl.module

val navigationModule = module {

    single<StoreFactory> { DefaultStoreFactory() }
    single { OnlineImagesStoreFactory(get() , get()) }
    single { LocalImagesStoreFactory(get() , get()) }
    single { ResourcesImagesFactory(get() , get()) }
}