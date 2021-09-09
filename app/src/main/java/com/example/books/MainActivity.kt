package com.example.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold

import androidx.compose.material.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil.compose.LocalImageLoader
import coil.imageLoader
import com.example.books.navigation.addBottomNavigationDestinations
import com.example.bottonnavigation.BookBottomNavigation
import com.example.bottonnavigation.SearchRoute

import com.example.composeextensions.AssistedHiltInjectables
import com.example.navigator.Navigator
import com.example.navigator.NavigatorEvent
import com.example.onetimepreferences.OneTimePreferenceViewModel
import com.example.runcodeeveryxlaunch.RunCodePreferencesViewModel
import com.example.settingdata.SettingsViewModel

import com.example.theme.BooksTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), AssistedHiltInjectables {

    @Inject
    override lateinit var runCodePreferencesViewModelFactory: RunCodePreferencesViewModel.RunCodePreferencesViewModelFactory

    @Inject
    override lateinit var  oneTimePreferencesViewModel: OneTimePreferenceViewModel.OneTimePreferenceViewModelFactory

    @Inject
    lateinit var navigator: Navigator

    //private val isDarkThemeEnabled get() = AppCompatDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BooksTheme(
                darkThemeFlow = hiltViewModel<SettingsViewModel>().darkTheme,
                defaultValue = false
            ) {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                        Surface(color = MaterialTheme.colors.background) {
                            BookScaffold(navigator = navigator)
                        }
                    }
                }
            }
        }
    }


}



@Composable
fun BookScaffold(navigator: Navigator){
    val navController = rememberNavController()

    LaunchedEffect(navController){
        navigator.destinations.collect {
            when(val event = it){
                is NavigatorEvent.NavigateUp -> navController.navigateUp()
                is NavigatorEvent.Directions -> navController.navigate(
                    event.destination,
                    event.builder
                )
            }
        }
    }

    Scaffold(
        bottomBar = {
            BookBottomNavigation(navController = navController, emptyList())
        }
    ){
        NavHost(
            navController = navController ,
            startDestination = SearchRoute.route,
            builder ={
                addBottomNavigationDestinations()
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}
