package com.example.books.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.bottonnavigation.*
import com.example.favoritebookui.Favorites
import com.example.latestbookui.LatestBooks
import com.example.searchui.SearchUI
import com.example.settingui.Settings

private val destinationsBottomNav: Map<BottomNavigationEntry, @Composable () -> Unit> = mapOf(
    SearchRoute to { SearchUI()},
    FavoritesRoute to { Favorites()},
    LatestRoute to { LatestBooks()},
    SettingRoute to { Settings()}
)


fun NavGraphBuilder.addBottomNavigationDestinations(){
    destinationsBottomNav.forEach{entry ->
        val destination = entry.key
        composable(destination.route){
            entry.value()
        }
    }
}