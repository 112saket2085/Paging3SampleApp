package com.example.paging3sampleapp.util

import android.content.Context
import android.widget.Toast


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



