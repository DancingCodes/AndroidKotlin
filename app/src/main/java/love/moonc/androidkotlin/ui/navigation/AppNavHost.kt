package love.moonc.androidkotlin.ui.navigation

import love.moonc.androidkotlin.ui.screens.EditProfileScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    userPreferences: UserPreferences // 💡 改为外部传入，不要在内部 new
) {
    // 移除这行：val userPreferences = UserPreferences(context)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        // 使用固定的 innerPadding，不要再加 if 判断，Scaffold 会自动处理高度
        modifier = Modifier.padding(innerPadding)
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
                                popUpTo(0) { inclusive = true } // 💡 注册成功清空所有栈
                            }
                        }
                    } else {
                        authViewModel.login(LoginRequest(account, password)) {
                            navController.navigate(Screen.HOME) {
                                popUpTo(0) { inclusive = true } // 💡 登录成功清空所有栈
                            }
                        }
                    }
                }
            )
        }

        composable(Screen.HOME) { HomeScreen() }
        composable(Screen.DISCOVER) { DiscoverScreen(navController) }
        composable(Screen.PROFILE) { ProfileScreen(navController) }
        composable(Screen.BOTTLE) { BottleScreen(navController) }
        composable(Screen.EDIT_PROFILE) { EditProfileScreen(navController) }
        composable(Screen.MODIFY_AVATAR) { ModifyAvatarScreen(navController) }
        composable(Screen.MODIFY_NICKNAME) { ModifyNicknameScreen(navController) }
        composable(Screen.MODIFY_SIGNATURE) { ModifySignatureScreen(navController) }
        composable(Screen.MODIFY_PASSWORD) { ModifyPasswordScreen(navController) }
        composable(Screen.INVITE) { InviteScreen() }
        composable(Screen.SETTINGS) { SettingsScreen(navController) }
    }
}