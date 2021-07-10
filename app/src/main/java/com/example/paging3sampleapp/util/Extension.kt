package com.example.paging3sampleapp.util

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore


fun Context.showToastMsg(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}

fun Array<out String>.getString(): String {
    var temp = ""
    for (log in this) {
        temp = "$temp$log"
    }
    return temp
}

val Context.dataStore by preferencesDataStore(name = "settings")

fun Window.setFullscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

fun Window.removeFullscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.show(WindowInsets.Type.statusBars())
    } else {
        setFlags(
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
        )
    }
}

