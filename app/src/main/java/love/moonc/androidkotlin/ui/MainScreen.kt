package love.moonc.androidkotlin.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.ui.navigation.AppNavHost
import love.moonc.androidkotlin.ui.navigation.Screen
import love.moonc.androidkotlin.ui.navigation.mainTabs

@Composable
fun MainScreen() {
    val context = LocalContext.current
    // 使用 remember 确保不会在重组时重复创建对象
    val userPreferences = remember { UserPreferences(context) }

    // 关键点：将初始值设为 false
    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isMainTab = mainTabs.any { it.route == currentRoute }

    // 直接渲染 Scaffold，不再使用 if (isLoggedIn != null)
    Scaffold(
        bottomBar = {
            if (isMainTab) {
                TabBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
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
            // 这里的 isLoggedIn 会随着 DataStore 读取完成从 false 变为 true（如果已登录）
            startDestination = if (isLoggedIn) Screen.HOME else Screen.AUTH
        )
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