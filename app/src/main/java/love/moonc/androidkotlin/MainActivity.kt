package love.moonc.androidkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import love.moonc.androidkotlin.ui.components.TabBar
import love.moonc.androidkotlin.ui.components.TabItem
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.DiscoverScreen
import love.moonc.androidkotlin.ui.screens.ProfileScreen
import love.moonc.androidkotlin.ui.screens.BottleScreen
import love.moonc.androidkotlin.ui.theme.AndroidKotlinTheme

object Screen {
    // 一级页面
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val PROFILE = "profile"

    // 二级页面（详情、工具页等）
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
    // 1. 抽离配置数据，使用 remember 避免重复创建
    val tabs = remember {
        listOf(
            TabItem("首页", Icons.Default.Home, Screen.HOME),
            TabItem("发现", Icons.Default.Menu, Screen.DISCOVER),
            TabItem("我的", Icons.Default.Person, Screen.PROFILE)
        )
    }

    val navController = rememberNavController()

    // 2. 找回这个状态变量：它是动画立即触发的关键
    var selectedIndex by remember { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 3. 监听路由变化：确保点击“物理返回键”时，底栏高亮也能同步跳回来
    LaunchedEffect(currentRoute) {
        val index = tabs.indexOfFirst { it.route == currentRoute }
        if (index != -1) {
            selectedIndex = index
        }
    }

    // 判断是否为主界面
    val isMainScreen = tabs.any { it.route == currentRoute }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isMainScreen) {
                TabBar(
                    items = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        selectedIndex = index
                        val targetRoute = tabs[index].route
                        navController.navigate(targetRoute) {
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
            // 只有主界面才加内边距，二级页面如漂流瓶直接全屏
            modifier = Modifier.padding(if (isMainScreen) innerPadding else PaddingValues(0.dp))
        ) {
            composable(Screen.HOME) { HomeScreen() }
            composable(Screen.DISCOVER) { DiscoverScreen(navController = navController) }
            composable(Screen.PROFILE) { ProfileScreen() }
            composable(Screen.BOTTLE) { BottleScreen(navController = navController) }
        }
    }
}