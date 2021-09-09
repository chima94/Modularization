package com.example.downloadsdestination

import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import androidx.navigation.navDeepLink
import com.example.navigator.NavigationDestination


const val DOWNLOADS = "downloads"

object DownloadsDestination : NavigationDestination{

    override fun route(): String = DOWNLOADS

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(BOOK_ID_PARAM){
            type = NavType.StringType
            nullable = true
        }
    )


    override val deepLinks: List<NavDeepLink> = listOf(
        navDeepLink {
            uriPattern = DOWNLOADED_BOOK_NAME_URI_PATTERN
        }
    )

}