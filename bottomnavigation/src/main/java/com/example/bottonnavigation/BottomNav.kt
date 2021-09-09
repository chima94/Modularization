package com.example.bottonnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.downloadsdestination.DOWNLOADS


object SearchRoute : BottomNavigationEntry(SEARCH, com.example.strings.R.string.search)
object DownloadRoute: BottomNavigationEntry(DOWNLOADS, com.example.strings.R.string.download)
object LatestRoute: BottomNavigationEntry(LATEST_BOOKS, com.example.strings.R.string.latest)
object FavoritesRoute: BottomNavigationEntry(FAVORITES, com.example.strings.R.string.favorites)
object SettingRoute: BottomNavigationEntry(SETTINGS, com.example.strings.R.string.setting)

val bottomNavigationEntries = listOf(
    BottomNavigationUIEntry(
        SearchRoute,
        Icons.Filled.Search
    ),
    BottomNavigationUIEntry(
       DownloadRoute,
       Icons.Filled.Download
    ),
    BottomNavigationUIEntry(
        FavoritesRoute,
        Icons.Filled.Favorite
    ),
    BottomNavigationUIEntry(
        LatestRoute,
        Icons.Filled.List
    ),
    BottomNavigationUIEntry(
        SettingRoute,
        Icons.Filled.Settings
    )
)