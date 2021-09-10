package com.example.books.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.navigator.NavigationDestination
import com.example.searchresultdestination.SearchResultDestination
import com.example.searchresultui.SearchResultUI

private val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
    SearchResultDestination to { SearchResultUI()}
)


fun NavGraphBuilder.addComposableDestination(){
    composableDestinations.forEach { entry ->
        val destination = entry.key
        composable(destination.route(), destination.arguments, destination.deepLinks){
            entry.value
        }
    }
}