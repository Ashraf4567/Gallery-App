package com.example.galleryapp.presentation.screens.onlineImages

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.galleryapp.domain.Image
import com.example.galleryapp.domain.ImagesRepository
import com.example.galleryapp.domain.ImagesState
import com.example.galleryapp.utls.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias OnlineImagesState = ImagesState<Image>

sealed interface OnlineImagesAction{
    data object LoadImages : OnlineImagesAction
}

private sealed interface OnlineImagesMsg{
    class UpdateIsLoading(val isLoading: Boolean) : OnlineImagesMsg
    class UpdateSuccess(val images: List<Image>) : OnlineImagesMsg
    class UpdateError(val error: String) : OnlineImagesMsg
}


interface OnlineImageStore: Store<Nothing, OnlineImagesState , Nothing>

class OnlineImagesStoreFactory(
    private val storeFactory: StoreFactory,
    private val imagesRepository: ImagesRepository
){
    fun create(): OnlineImageStore = object : OnlineImageStore, Store<Nothing, OnlineImagesState, Nothing> by storeFactory
        .create(
            name = "ImagesStoreFactory",
            initialState = OnlineImagesState(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ){}

    private class BootstrapperImpl : CoroutineBootstrapper<OnlineImagesAction>(){
        override fun invoke() {
            scope.launch {
                Log.d("BootstrapperImpl", "invoke: ")
                dispatch(OnlineImagesAction.LoadImages)
            }
        }
    }

    private object ReducerImpl : Reducer<OnlineImagesState, OnlineImagesMsg> {
        override fun OnlineImagesState.reduce(msg: OnlineImagesMsg): OnlineImagesState {
            return when(msg){
                is OnlineImagesMsg.UpdateIsLoading -> copy(isLoading = msg.isLoading)
                is OnlineImagesMsg.UpdateSuccess -> copy(images = msg.images)
                is OnlineImagesMsg.UpdateError -> copy(error = msg.error)
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Nothing, OnlineImagesAction, OnlineImagesState, OnlineImagesMsg, Nothing>(){
        override fun executeAction(action: OnlineImagesAction, getState: () -> OnlineImagesState) {
            when(action){
                OnlineImagesAction.LoadImages ->{
                    scope.launch(Dispatchers.IO) {
                        imagesRepository.getImages().collect{result ->
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
    }


}
