package com.example.bottonnavigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shape.BottomSheetShapes
import com.google.accompanist.insets.navigationBarsPadding


@Composable
fun BookBottomNavigation(
    navController: NavHostController,
    hideBottomNavOnDestination: List<String> = emptyList()
){

    var hideBottomNav by rememberSaveable { mutableStateOf(false)}
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val size = if(hideBottomNav){
        Modifier.size(animateDpAsState(targetValue = 0.dp, animationSpec = tween()).value)
    }else{
        Modifier
    }

    BottomNavigation(
        modifier = size
            .clip(BottomSheetShapes.large)
            .navigationBarsPadding()
    ) {
        hideBottomNav = currentRoute in hideBottomNavOnDestination
        bottomNavigationEntries.forEach { bottomEntry ->
            BottomNavigationItem(
                selected = currentRoute == bottomEntry.screen.route,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(bottomEntry.screen.route){
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                label = {
                    Text(
                        modifier = Modifier.wrapContentSize(unbounded = true),
                        softWrap = false,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        text = stringResource(id = bottomEntry.screen.resourcesID)
                    )
                },
                icon = {
                    Icon(
                        imageVector = bottomEntry.icon,
                        contentDescription = stringResource(id = bottomEntry.screen.resourcesID)
                    )
                }
            )
        }
    }
}