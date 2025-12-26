package love.moonc.androidkotlin.ui.navigation

import love.moonc.androidkotlin.ui.screens.EditProfileScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import love.moonc.androidkotlin.data.LoginRequest
import love.moonc.androidkotlin.data.RegisterRequest
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.ui.screens.AuthScreen
import love.moonc.androidkotlin.ui.screens.AuthViewModel
import love.moonc.androidkotlin.ui.screens.BottleScreen
import love.moonc.androidkotlin.ui.screens.DiscoverScreen
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.InviteScreen
import love.moonc.androidkotlin.ui.screens.ModifyAvatarScreen
import love.moonc.androidkotlin.ui.screens.ProfileScreen
import love.moonc.androidkotlin.ui.screens.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    isMainTab: Boolean,
    startDestination: String
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(if (isMainTab) innerPadding else PaddingValues())
    ) {
        composable(Screen.AUTH) {
            val authViewModel: AuthViewModel = viewModel(
                initializer = {
                    AuthViewModel(userPreferences)
                }
            )

            AuthScreen(
                isLoading = authViewModel.isLoading,
                onAuthClick = { isRegister, nickname, account, password ->
                    if (isRegister) {
                        authViewModel.register(RegisterRequest(nickname, account, password)) {
                            navController.navigate(Screen.HOME) {
                                popUpTo(Screen.AUTH) { inclusive = true }
                            }
                        }
                    }else {
                        authViewModel.login(LoginRequest(account, password)) {
                            navController.navigate(Screen.HOME) {
                                popUpTo(Screen.AUTH) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // --- 主页 Tabs (需要 TabBar 的内边距) ---
        composable(Screen.HOME) { HomeScreen() }
        composable(Screen.DISCOVER) { DiscoverScreen(navController) }
        composable(Screen.PROFILE) { ProfileScreen(navController) }

        // --- 其他全屏或二级页面 ---
        composable(Screen.BOTTLE) { BottleScreen(navController) }
        composable(Screen.EDIT_PROFILE) { EditProfileScreen(navController) }
        composable(Screen.MODIFY_AVATAR) { ModifyAvatarScreen(navController) }
        composable(Screen.MODIFY_NICKNAME) { /* 你之后写的昵称修改页 */ }
        composable(Screen.INVITE) { InviteScreen() }
        composable(Screen.SETTINGS) { SettingsScreen() }
    }
}
