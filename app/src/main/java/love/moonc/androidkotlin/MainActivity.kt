package love.moonc.androidkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import love.moonc.androidkotlin.ui.screens.BottleScreen
import love.moonc.androidkotlin.ui.screens.DiscoverScreen
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.ProfileScreen
import love.moonc.androidkotlin.ui.theme.AndroidKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidKotlinTheme {
                MainScreen()
            }
        }
    }
}

object Screen {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val PROFILE = "profile"
    const val BOTTLE = "bottle"
}

private data class TabItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val tabs = remember {
        listOf(
            TabItem("首页", Icons.Default.Home, Screen.HOME),
            TabItem("发现", Icons.Default.Menu, Screen.DISCOVER),
            TabItem("我的", Icons.Default.Person, Screen.PROFILE)
        )
    }

    // 判断当前路由是否在 Tab 列表中
    val isMainTab = tabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (isMainTab) {
                TabBar(
                    tabs = tabs,
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        navController.navigate(route) {
                            // 弹出到导航图的起始目的地，避免在堆栈中堆积大量页面
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // 避免多次点击同一个 Tab 时重复创建实例
                            launchSingleTop = true
                            // 恢复之前保存的状态（如滚动位置）
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HOME,
            // 只有在主 Tab 页面才应用 innerPadding (底部栏占据的空间)
            // 这样二级页面（如 BOTTLE）就可以实现全屏效果
            modifier = Modifier.padding(if (isMainTab) innerPadding else PaddingValues())
        ) {
            composable(Screen.HOME) { HomeScreen() }
            composable(Screen.DISCOVER) { DiscoverScreen(navController) }
            composable(Screen.PROFILE) { ProfileScreen() }

            // 独立的二级页面
            composable(Screen.BOTTLE) {
                BottleScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun TabBar(
    tabs: List<TabItem>,
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        tabs.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onTabSelected(item.route) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}