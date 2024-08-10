package com.example.galleryapp.presentation.screens.resourceImages

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.galleryapp.data.ImagesRepository
import com.example.galleryapp.data.model.ImagesState
import com.example.galleryapp.utls.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias ResourcesImagesState = ImagesState<Int>

sealed interface ResourcesImagesAction{
    data object LoadImage: ResourcesImagesAction
}

private sealed interface ResourcesImagesMsg {
    class UpdateIsLoading(val isLoading: Boolean) : ResourcesImagesMsg
    class UpdateSuccess(val imagesRes: List<Int>) : ResourcesImagesMsg
    class UpdateError(val error: String) : ResourcesImagesMsg
}

interface ResourcesImagesStore: Store<Nothing, ResourcesImagesState, Nothing>

class ResourcesImagesFactory(private val storeFactory: StoreFactory){

    fun create(): ResourcesImagesStore = object : ResourcesImagesStore, Store<Nothing, ResourcesImagesState, Nothing> by storeFactory
        .create(
            name = "ResourcesImagesStore",
            initialState = ResourcesImagesState(),
            bootstrapper = SimpleBootstrapper(ResourcesImagesAction.LoadImage) ,
            executorFactory =::ExecutorImpl ,
            reducer = ReducerImpl
        ){}


    private object ReducerImpl : Reducer<ResourcesImagesState, ResourcesImagesMsg> {
        override fun ResourcesImagesState.reduce(msg: ResourcesImagesMsg): ResourcesImagesState {
            return when(msg){
                is ResourcesImagesMsg.UpdateError -> copy(error = msg.error)
                is ResourcesImagesMsg.UpdateIsLoading -> copy(isLoading = msg.isLoading)
                is ResourcesImagesMsg.UpdateSuccess -> copy(images = msg.imagesRes)
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Nothing , ResourcesImagesAction , ResourcesImagesState , ResourcesImagesMsg , Nothing>(){
        override fun executeAction(
            action: ResourcesImagesAction,
            getState: () -> ResourcesImagesState
        ) {
            when(action){
                ResourcesImagesAction.LoadImage -> handleLoadImages()


            }
        }

        private fun handleLoadImages() {
            scope.launch(Dispatchers.IO) {
                ImagesRepository.getResourcesImages().collect{ result ->
                    when(result){
                        is DataState.Error -> {
                            withContext(Dispatchers.Main){
                                dispatch(ResourcesImagesMsg.UpdateError(result.message?:"Something went wrong"))
                            }
                        }
                        is DataState.Loading -> {
                            withContext(Dispatchers.Main){
                                dispatch(ResourcesImagesMsg.UpdateIsLoading(true))
                            }
                        }
                        is DataState.Success -> {
                            withContext(Dispatchers.Main){
                                result.data?.let {
                                    dispatch(ResourcesImagesMsg.UpdateSuccess(it))
                                    dispatch(ResourcesImagesMsg.UpdateIsLoading(false))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}