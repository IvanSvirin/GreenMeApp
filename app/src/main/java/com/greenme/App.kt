package com.greenme

import android.app.Application
import android.content.Context
import com.greenme.datasource.cache.CacheLibrary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        CacheLibrary.init(this)

        startKoin { androidContext(this@App) }

    }
}