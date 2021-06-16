package com.example.paging3sampleapp.util

import android.util.Log
import com.example.paging3sampleapp.BuildConfig
import com.example.paging3sampleapp.app.AppConstants

object Logger {

    inline fun <reified T> log(loggerType: LogType, vararg msg: String) {
        if (BuildConfig.BUILD_TYPE == AppConstants.DEBUG_TYPE) {
            val tag = T::class.java.simpleName
            when (loggerType) {
                LogType.DEBUG -> Log.d(tag, msg.getString())
                LogType.INFO -> Log.i(tag, msg.getString())
                LogType.ERROR -> Log.e(tag, msg.getString())
            }
        }
    }

    enum class LogType {
        DEBUG,
        INFO,
        ERROR
    }
}