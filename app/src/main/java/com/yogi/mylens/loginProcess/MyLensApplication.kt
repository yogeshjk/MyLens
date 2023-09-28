package com.yogi.mylens.loginProcess

import android.app.Application


class MyLensApplication :Application() {
    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }
}