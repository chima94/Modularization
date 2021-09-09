package com.example.bottonnavigation

import androidx.annotation.StringRes

sealed class BottomNavigationEntry(val route: String, @StringRes val resourcesID: Int){
    companion object{
        const val SEARCH = "search"
        const val FAVORITES = "favorites"
        const val LATEST_BOOKS = "latestBooks"
        const val SETTINGS = "settings"
    }
}