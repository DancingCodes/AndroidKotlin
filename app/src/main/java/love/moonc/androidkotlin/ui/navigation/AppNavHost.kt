package love.moonc.androidkotlin.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.ui.screens.AuthScreen
import love.moonc.androidkotlin.ui.screens.AuthViewModel
import love.moonc.androidkotlin.ui.screens.BottleScreen
import love.moonc.androidkotlin.ui.screens.DiscoverScreen
import love.moonc.androidkotlin.ui.screens.EditProfileScreen
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.InviteScreen
import love.moonc.androidkotlin.ui.screens.ModifyAvatarScreen
import love.moonc.androidkotlin.ui.screens.ModifyNicknameScreen
import love.moonc.androidkotlin.ui.screens.ModifyPasswordScreen
import love.moonc.androidkotlin.ui.screens.ModifySignatureScreen
import love.moonc.androidkotlin.ui.screens.ProfileScreen
import love.moonc.androidkotlin.ui.screens.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String,
    userPreferences: UserPreferences
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Auth.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userPreferences)
            )

            AuthScreen(
                viewModel = authViewModel, // 直接传 VM 进去，内部自给自足
                onAuthSuccess = {
                    // 导航器只管：成功了就带路，不问中间发生了什么
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
        composable(Screen.Profile.Settings.route) {SettingsScreen(navController)}
        composable(Screen.Profile.Edit.route) { EditProfileScreen(navController) }
        composable(Screen.Profile.Edit.Avatar.route) { ModifyAvatarScreen(navController) }
        composable(Screen.Profile.Edit.Nickname.route) { ModifyNicknameScreen(navController) }
        composable(Screen.Profile.Edit.Signature.route) { ModifySignatureScreen(navController) }
        composable(Screen.Profile.Edit.Password.route) { ModifyPasswordScreen(navController) }
    }
}

/**
 * ViewModel 工厂，用于向 ViewModel 注入依赖
 */
class AuthViewModelFactory(private val prefs: UserPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}