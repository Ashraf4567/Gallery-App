package com.example.galleryapp.presentation.screens.localImages

import com.arkivanov.decompose.ComponentContext

class LocalImagesComponent(
    componentContext: ComponentContext,
    localImageStoreFactory : LocalImagesStoreFactory,
    private val onGoBack: () -> Unit,
    private val onGoNext: () -> Unit
): ComponentContext by componentContext {

    val store = localImageStoreFactory.create()

    fun onBackClicked() {
        onGoBack()
    }

    fun onNextClicked() {
        onGoNext()
    }

}