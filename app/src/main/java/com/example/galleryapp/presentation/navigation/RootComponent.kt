package com.example.galleryapp.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.example.galleryapp.presentation.screens.localImages.LocalImagesComponent
import com.example.galleryapp.presentation.screens.localImages.LocalImagesStoreFactory
import com.example.galleryapp.presentation.screens.onlineImages.OnlineImagesStoreFactory
import com.example.galleryapp.presentation.screens.onlineImages.OnlineScreenComponent
import com.example.galleryapp.presentation.screens.resourceImages.ResourcesImagesFactory
import com.example.galleryapp.presentation.screens.resourceImages.ResourcesScreenComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
    private val onlineImageStore: OnlineImagesStoreFactory,
    private val localImageStore: LocalImagesStoreFactory,
    private val resourcesImageStore: ResourcesImagesFactory
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    val childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.OnlineScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            is Config.OnlineScreen -> Child.OnlineScreen(
                OnlineScreenComponent(
                componentContext = componentContext,
                navigateToScreen2 = { navigation.pushNew(Config.LocalScreen) },
                onlineImageStoreFactory = onlineImageStore
            )
            )
            is Config.LocalScreen -> Child.LocalScreen(LocalImagesComponent(
                componentContext = componentContext,
                onGoBack = { navigation.pop() },
                localImageStoreFactory = localImageStore,
                onGoNext = { navigation.pushNew(Config.ResourcesScreen) }
            ))

            Config.ResourcesScreen -> Child.ResourcesScreen(ResourcesScreenComponent(
                componentContext = componentContext,
                onButtonBackClicked = { navigation.pop() },
                resourcesImagesFactory = resourcesImageStore
            ))
        }
    }

    sealed class Child {
        class OnlineScreen(val component: OnlineScreenComponent) : Child()
        class LocalScreen(val component: LocalImagesComponent) : Child()
        class ResourcesScreen(val component: ResourcesScreenComponent) : Child()
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object OnlineScreen : Config()
        @Serializable
        data object LocalScreen : Config()
        @Serializable
        data object ResourcesScreen : Config()
    }

}