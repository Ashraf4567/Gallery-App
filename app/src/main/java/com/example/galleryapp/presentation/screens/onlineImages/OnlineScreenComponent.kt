package com.example.galleryapp.presentation.screens.onlineImages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle

// Component class that represents the online screen, handling navigation and store interactions
class OnlineScreenComponent(
    componentContext: ComponentContext,
    private val navigateToScreen2: () -> Unit,
    onlineImageStoreFactory: OnlineImagesStoreFactory,
): ComponentContext by componentContext {


    // Initialize the store with the provided factory
    val store = onlineImageStoreFactory.create()

    fun onButtonClick() = navigateToScreen2()

}