package com.example.galleryapp.presentation.screens.localImages

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.galleryapp.App
import com.example.galleryapp.domain.ImagesRepository
import com.example.galleryapp.domain.ImagesState
import com.example.galleryapp.utls.DataState
import com.example.galleryapp.utls.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

typealias LocalImagesState = ImagesState<Uri>

sealed interface LocalImagesAction{
    data object LoadImages: LocalImagesAction
}

private sealed interface LocalImagesMsg{
    class UpdateIsLoading(val isLoading: Boolean) : LocalImagesMsg
    class UpdateSuccess(val images: List<Uri>) : LocalImagesMsg
    class UpdateError(val error: String) : LocalImagesMsg
}

interface LocalImagesStore: Store<Nothing, LocalImagesState, Nothing>

class LocalImagesStoreFactory(
    private val storeFactory: StoreFactory,
    private val imagesRepository: ImagesRepository
){
    fun create(): LocalImagesStore = object : LocalImagesStore, Store<Nothing, LocalImagesState, Nothing> by storeFactory
        .create(
            name = "LocalImagesStore",
            initialState = LocalImagesState(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ){}

    private class BootstrapperImpl : CoroutineBootstrapper<LocalImagesAction>() {
        override fun invoke() {
            scope.launch {
                dispatch(LocalImagesAction.LoadImages)
            }
        }
    }

    private object ReducerImpl: Reducer<LocalImagesState , LocalImagesMsg>{
        override fun LocalImagesState.reduce(msg: LocalImagesMsg): LocalImagesState {
            return when(msg){
                is LocalImagesMsg.UpdateError -> copy(error = msg.error)
                is LocalImagesMsg.UpdateIsLoading -> copy(isLoading = msg.isLoading)
                is LocalImagesMsg.UpdateSuccess -> copy(images = msg.images)
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Nothing , LocalImagesAction , LocalImagesState , LocalImagesMsg, Nothing>(){
        override fun executeAction(action: LocalImagesAction, getState: () -> LocalImagesState) {
            when(action){
                LocalImagesAction.LoadImages -> handleLoadImages()
            }
        }

        private fun handleLoadImages() {
            scope.launch(Dispatchers.IO) {
                imagesRepository.getImages().collect{result ->
                    when(result){
                        is DataState.Error -> {
                            withContext(Dispatchers.Main){
                                dispatch(LocalImagesMsg.UpdateError(result.message?:"Unknown Error"))
                                dispatch(LocalImagesMsg.UpdateIsLoading(false))
                            }

                        }
                        is DataState.Loading -> {

                            withContext(Dispatchers.Main){
                                dispatch(LocalImagesMsg.UpdateIsLoading(true))
                            }

                        }
                        is DataState.Success -> {
                            //download images from internet then add them to cache directory and finally add them to the list
                            val loadedImagesUris = mutableListOf<Uri>()
                            result.data?.takeLast(3)?.forEach { image ->

                                val timestamp = System.currentTimeMillis()
                                val fileName = "${timestamp}_${image.downloadUrl?.substringAfterLast("/")}"
                                val filePath = "${App.context.cacheDir}/$fileName"
                                Helper.downloadImage(image.downloadUrl ?: "", filePath)
                                val uri = Uri.fromFile(File(filePath))
                                loadedImagesUris.add(uri)
                            }
                            withContext(Dispatchers.Main) {
                                dispatch(LocalImagesMsg.UpdateSuccess(loadedImagesUris))
                                dispatch(LocalImagesMsg.UpdateIsLoading(false))
                            }

                        }
                    }
                }
            }
        }
    }
}