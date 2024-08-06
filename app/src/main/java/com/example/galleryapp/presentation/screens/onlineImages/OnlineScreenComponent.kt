package com.example.galleryapp.presentation.screens.onlineImages

import com.arkivanov.decompose.ComponentContext

class OnlineScreenComponent(
    componentContext: ComponentContext,
    private val navigateToScreen2: () -> Unit,
    onlineImageStoreFactory: OnlineImagesStoreFactory,
): ComponentContext by componentContext {


    val store = onlineImageStoreFactory.create()


    fun onButtonClick() = navigateToScreen2()

}