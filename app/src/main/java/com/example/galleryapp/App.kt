package com.example.galleryapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.galleryapp.di.apiModule
import com.example.galleryapp.di.navigationModule
import com.example.galleryapp.di.repoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {

    //need to make application context
    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(navigationModule , apiModule , repoModule)
        }
    }
}