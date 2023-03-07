package com.pu.demo.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.facebook.drawee.backends.pipeline.Fresco
import com.pu.demo.app.event.AppViewModel
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application(), ViewModelStoreOwner {

    private lateinit var viewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        viewModelStore = ViewModelStore()
        appVM = ViewModelProvider(this)[AppViewModel::class.java]

        MMKV.initialize(this)
        Fresco.initialize(this)

        XLog.init(
            LogConfiguration.Builder().logLevel(LogLevel.ALL)
                .tag("chat")
                .enableThreadInfo()
                .enableBorder()
                .build(),
            AndroidPrinter()
        )
    }

    companion object {
        lateinit var INSTANCE: App
        lateinit var appVM: AppViewModel
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

}