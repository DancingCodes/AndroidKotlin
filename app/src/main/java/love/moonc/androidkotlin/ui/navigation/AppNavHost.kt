package love.moonc.androidkotlin.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import love.moonc.androidkotlin.ui.screens.* @Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    isMainTab: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME,
        modifier = Modifier.padding(if (isMainTab) innerPadding else PaddingValues()),
        enterTransition = { fadeIn() + scaleIn() },
        exitTransition = { fadeOut() + scaleOut() }
    ) {
        composable(Screen.HOME) { HomeScreen() }
        composable(Screen.DISCOVER) { DiscoverScreen(navController) }
        composable(Screen.PROFILE) { ProfileScreen() }
        composable(Screen.BOTTLE) { BottleScreen(navController) }
    }
}