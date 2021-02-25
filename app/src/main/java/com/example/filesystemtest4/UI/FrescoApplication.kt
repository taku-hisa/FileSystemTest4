package com.example.filesystemtest4.UI

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class FrescoApplication: Application()  {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}