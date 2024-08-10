package com.example.galleryapp

import android.app.Application
import android.content.Context

class App: Application() {

    //need to make application context
    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }
}