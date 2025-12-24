package love.moonc.androidkotlin.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import love.moonc.androidkotlin.ui.screens.BottleScreen
import love.moonc.androidkotlin.ui.screens.DiscoverScreen
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.InviteScreen
import love.moonc.androidkotlin.ui.screens.ProfileScreen
import love.moonc.androidkotlin.ui.screens.SettingsScreen

@Composable
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
        composable(Screen.PROFILE) { ProfileScreen(navController) }
        composable(Screen.BOTTLE) { BottleScreen(navController) }
        composable(Screen.INVITE) { InviteScreen() }
        composable(Screen.SETTINGS) { SettingsScreen() }
    }
}