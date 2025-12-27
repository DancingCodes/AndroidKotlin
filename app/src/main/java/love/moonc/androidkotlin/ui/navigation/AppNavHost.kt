package love.moonc.androidkotlin.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import love.moonc.androidkotlin.ui.screens.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Auth.route) {
            val authViewModel: AuthViewModel = hiltViewModel()

            AuthScreen(
                viewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Discover.route) { DiscoverScreen(navController) }
        composable(Screen.Discover.Bottle.route) { BottleScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Profile.Invite.route) { InviteScreen() }
        composable(Screen.Profile.Settings.route) { SettingsScreen(navController) }
        composable(Screen.Profile.Edit.route) { EditProfileScreen(navController) }
        composable(Screen.Profile.Edit.Avatar.route) { ModifyAvatarScreen(navController) }
        composable(Screen.Profile.Edit.Nickname.route) { ModifyNicknameScreen(navController) }
        composable(Screen.Profile.Edit.Signature.route) { ModifySignatureScreen(navController) }
        composable(Screen.Profile.Edit.Password.route) { ModifyPasswordScreen(navController) }
    }
}
