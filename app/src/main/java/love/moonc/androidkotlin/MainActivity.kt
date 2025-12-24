package love.moonc.androidkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import love.moonc.androidkotlin.ui.components.TabBar
import love.moonc.androidkotlin.ui.components.TabItem
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.DiscoverScreen
import love.moonc.androidkotlin.ui.screens.ProfileScreen
import love.moonc.androidkotlin.ui.screens.BottleScreen // 别忘了导入新页面
import love.moonc.androidkotlin.ui.theme.AndroidKotlinTheme

// 定义路由常量
object Screen {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val PROFILE = "profile"
    const val BOTTLE = "bottle"
}

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

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }

    // 关键：监听导航堆栈变化，自动同步底栏高亮
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 根据当前路由自动更新选中的索引
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Screen.HOME -> selectedIndex = 0
            Screen.DISCOVER -> selectedIndex = 1
            Screen.PROFILE -> selectedIndex = 2
        }
    }

    val tabs = listOf(
        TabItem("首页", Icons.Default.Home),
        TabItem("发现", Icons.Default.Menu),
        TabItem("我的", Icons.Default.Person)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // 只有在主页面才显示底部导航栏
            if (currentRoute != Screen.BOTTLE) {
                TabBar(
                    items = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        val targetRoute = when (index) {
                            0 -> Screen.HOME
                            1 -> Screen.DISCOVER
                            else -> Screen.PROFILE
                        }
                        navController.navigate(targetRoute) {
                            // 避免重复创建页面栈
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
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
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HOME) { HomeScreen() }
            composable(Screen.DISCOVER) { DiscoverScreen(navController) }
            composable(Screen.PROFILE) { ProfileScreen() }
            composable(Screen.BOTTLE) { BottleScreen(navController) }
        }
    }
}