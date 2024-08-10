package com.example.galleryapp.presentation.screens.onlineImages

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.galleryapp.data.ImagesRepository
import com.example.galleryapp.data.model.Image
import com.example.galleryapp.data.model.ImagesState
import com.example.galleryapp.utls.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias OnlineImagesState = ImagesState<Image>

// Sealed interface representing the possible actions that can be taken on online images
sealed interface OnlineImagesAction{
    data object LoadImages : OnlineImagesAction
}

// representing the messages that can be used to update the state
private sealed interface OnlineImagesMsg{
    class UpdateIsLoading(val isLoading: Boolean) : OnlineImagesMsg
    class UpdateSuccess(val images: List<Image>) : OnlineImagesMsg
    class UpdateError(val error: String) : OnlineImagesMsg
}


interface OnlineImageStore: Store<Nothing, OnlineImagesState , Nothing>

class OnlineImagesStoreFactory(private val storeFactory: StoreFactory){


    // Factory method for creating an instance of the OnlineImageStore
    fun create(): OnlineImageStore = object : OnlineImageStore, Store<Nothing, OnlineImagesState, Nothing> by storeFactory
        .create(
            name = "ImagesStoreFactory",
            initialState = OnlineImagesState(),
            bootstrapper = SimpleBootstrapper(OnlineImagesAction.LoadImages),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ){}

    // Reducer implementation for handling the different messages
    private object ReducerImpl : Reducer<OnlineImagesState, OnlineImagesMsg> {
        override fun OnlineImagesState.reduce(msg: OnlineImagesMsg): OnlineImagesState {
            return when(msg){
                is OnlineImagesMsg.UpdateIsLoading -> copy(isLoading = msg.isLoading)
                is OnlineImagesMsg.UpdateSuccess -> copy(images = msg.images)
                is OnlineImagesMsg.UpdateError -> copy(error = msg.error)
            }
        }
    }

    // Executor implementation for handling the actions and updating the state
    private inner class ExecutorImpl : CoroutineExecutor<Nothing, OnlineImagesAction, OnlineImagesState, OnlineImagesMsg, Nothing>(){
        override fun executeAction(action: OnlineImagesAction, getState: () -> OnlineImagesState) {
            when(action){
                OnlineImagesAction.LoadImages -> handleLoadImages()
            }
        }

        private fun handleLoadImages() {
            scope.launch(Dispatchers.IO) {
                ImagesRepository.getImages().collect{ result ->
                    when(result){
                        is DataState.Error -> {
                            withContext(Dispatchers.Main){
                                dispatch(OnlineImagesMsg.UpdateError(result.message?:"Unknown Error"))
                                dispatch(OnlineImagesMsg.UpdateIsLoading(false))
                            }

                        }
                        is DataState.Loading -> {
                            withContext(Dispatchers.Main){
                                dispatch(OnlineImagesMsg.UpdateIsLoading(true))
                            }

                        }
                        is DataState.Success -> {
                            withContext(Dispatchers.Main){
                                result.data.let {list ->
                                    dispatch(OnlineImagesMsg.UpdateSuccess(list?: emptyList()))
                                }
                                dispatch(OnlineImagesMsg.UpdateIsLoading(false))
                            }
                        }
                    }
                }
            }
        }
    }

}
