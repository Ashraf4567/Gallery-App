package com.example.galleryapp.presentation.screens.onlineImages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.example.galleryapp.presentation.components.MyTopBar

@Composable
fun OnlineImageScreenContent(
    component: OnlineScreenComponent,
    modifier: Modifier = Modifier
) {
    val state = component.store.states.collectAsState(initial = OnlineImagesState())
    Scaffold(
        topBar = {
            MyTopBar(title = "Online Images" , onBackPressed = {} , showBackButton = false)
        }
    ) {paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){

            if (state.value.error.isNotEmpty()){
                Text(text = state.value.error)
            }

            if (state.value.isLoading){
                CircularProgressIndicator()
            }else{

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(state.value.images){
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.downloadUrl)
                                    .crossfade(true)
                                    .build()
                                ,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(200.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                Button(
                    onClick = { component.onButtonClick() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(text = "Next ->")
                }

            }

        }
    }

}