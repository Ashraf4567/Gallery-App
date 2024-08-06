package com.example.galleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.retainedComponent
import com.example.galleryapp.presentation.screens.localImages.LocalImagesScreen
import com.example.galleryapp.presentation.navigation.RootComponent
import com.example.galleryapp.presentation.screens.localImages.LocalImagesStoreFactory
import com.example.galleryapp.presentation.screens.onlineImages.OnlineImagesStoreFactory
import com.example.galleryapp.presentation.screens.onlineImages.OnlineImageScreenContent
import com.example.galleryapp.presentation.screens.resourceImages.ResourcesImagesFactory
import com.example.galleryapp.presentation.screens.resourceImages.ResourcesImagesScreen
import com.example.galleryapp.ui.theme.GalleryAppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onLineImagesStoreFactory: OnlineImagesStoreFactory by inject()
        val localImagesStoreFactory: LocalImagesStoreFactory by inject()
        val resourcesImagesStoreFactory: ResourcesImagesFactory by inject()
        val root = retainedComponent {
            RootComponent(
                componentContext = it,
                onlineImageStore = onLineImagesStoreFactory,
                localImageStore = localImagesStoreFactory,
                resourcesImageStore = resourcesImagesStoreFactory
            )
        }
        setContent {
            GalleryAppTheme {

                App(root)
            }
        }
    }
}

@Composable
fun App(rootComponent: RootComponent) {
        val childStack by rootComponent.childStack.subscribeAsState()

        Children(
            stack = childStack,
            animation = stackAnimation(slide()),
        ){child->
            when(val instance = child.instance){
                is RootComponent.Child.OnlineScreen -> OnlineImageScreenContent(component = instance.component)
                is RootComponent.Child.LocalScreen -> LocalImagesScreen(component = instance.component)
                is RootComponent.Child.ResourcesScreen -> ResourcesImagesScreen(component = instance.component)
            }
        }
}