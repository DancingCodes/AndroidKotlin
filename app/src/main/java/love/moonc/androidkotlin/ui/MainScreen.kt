package love.moonc.androidkotlin.ui

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import love.moonc.androidkotlin.data.NetworkManager
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.ui.navigation.AppNavHost
import love.moonc.androidkotlin.ui.navigation.Screen
import love.moonc.androidkotlin.ui.navigation.mainTabs


@Composable
fun MainScreen() {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    val tokenState by userPreferences.token.collectAsState(initial = null)

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isMainTab = mainTabs.any { it.route == currentRoute }

    LaunchedEffect(tokenState) {
        tokenState?.let { token ->
            if (token.isNotEmpty()) {
                try {
                    val response = NetworkManager.api.getProfile()
                    if (response.code == 200) {
                        val userData = response.data
                        if (userData != null) {
                            userPreferences.updateUser(userData.user)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // 3. 【核心修复】使用 when 明确分支，彻底消除编译器的 WHEN_CALL 歧义
    when (val token = tokenState) {
        null -> {
            // 状态一：正在从磁盘读取 DataStore，界面保持空白或显示启动图
            // 不要写 return，直接把这块区域留空或放个 Logo
        }

        else -> {
            // 状态二：已经读到结果了（无论是空字符串还是具体的 token）
            Scaffold(
                bottomBar = {
                    if (isMainTab) {
                        TabBar(
                            currentRoute = currentRoute,
                            onTabSelected = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    innerPadding = innerPadding,
                    isMainTab = isMainTab,
                    // 只要 token 不为空字符串，就视为已登录
                    startDestination = if (token.isNotEmpty()) Screen.HOME else Screen.AUTH
                )
            }
        }
    }
}

@Composable
private fun TabBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        mainTabs.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onTabSelected(item.route) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) }
            )
        }
    }
}