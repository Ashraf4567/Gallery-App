package com.example.galleryapp.presentation.screens.localImages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.example.galleryapp.presentation.components.MyTopBar


@Composable
fun LocalImagesScreen(
    modifier: Modifier = Modifier,
    component: LocalImagesComponent
) {
    val state = component.store.states.collectAsState(initial = LocalImagesState())

    Scaffold(
        topBar = {
            MyTopBar(title = "Local Images", onBackPressed = { component.onBackClicked() })
        }
    ) {paddingValues ->
        Box(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            if(state.value.isLoading){
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please wait while images are being loaded and cached on disk as uris...",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    LinearProgressIndicator()
                }
            }
            if (state.value.images.isNotEmpty()){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.value.images.size){index ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.value.images[index])
                                .build(),
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

            }
            Button(onClick = {
                component.onNextClicked()
            }, modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(text = "Next ->")
            }
        }
    }


}