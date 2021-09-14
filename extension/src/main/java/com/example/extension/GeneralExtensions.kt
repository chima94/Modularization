package com.example.extension

import android.content.Context
import android.content.Intent
import android.net.Uri

inline fun Context.openWebPage(url : String, onCantHandleAction : () -> Unit = {}){
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    try {
        startActivity(intent)
    }catch (t : Throwable){
        onCantHandleAction()
    }
}

fun String.removeBrackets(): String = replace("[", "", true)
    .replace("]", "", true)