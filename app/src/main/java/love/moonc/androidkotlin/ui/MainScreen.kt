package love.moonc.androidkotlin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    // 💡 2. 只有在第一次从磁盘加载数据时显示加载页
    if (tokenState == null) {
        LoadingScreen()
        return
    }

    // 💡 3. AppNavHost 常驻，不随 tokenState 变化而销毁重构
    Scaffold(
        bottomBar = {
            if (isMainTab) {
                TabBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        navController.navigate(route) {
                            // 确保底部栏跳转不会堆积栈
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
        // 获取当前真实的 token 状态（此时一定不是 null）
        val token = tokenState ?: ""

        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            startDestination = if (token.isNotEmpty()) Screen.HOME else Screen.AUTH,
            userPreferences = userPreferences
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "正在初始化应用...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
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