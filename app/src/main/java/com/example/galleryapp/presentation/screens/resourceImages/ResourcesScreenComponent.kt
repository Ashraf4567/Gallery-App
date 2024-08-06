package com.example.galleryapp.presentation.screens.resourceImages

import com.arkivanov.decompose.ComponentContext

class ResourcesScreenComponent(
    componentContext: ComponentContext,
    private val onButtonBackClicked: () -> Unit,
    resourcesImagesFactory: ResourcesImagesFactory
) : ComponentContext by componentContext {

    val store = resourcesImagesFactory.create()

    fun onBackClicked() = onButtonBackClicked()

}