package com.example.navigator

import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NamedNavArgument

fun interface NavigationDestination {

    fun route(): String

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    val deepLinks: List<NavDeepLink>
        get() = emptyList()
}