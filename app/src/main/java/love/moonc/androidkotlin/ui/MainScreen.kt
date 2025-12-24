package love.moonc.androidkotlin.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import love.moonc.androidkotlin.ui.navigation.AppNavHost
import love.moonc.androidkotlin.ui.navigation.mainTabs

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isMainTab = mainTabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (isMainTab) {
                TabBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route -> navController.navigate(route) }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            isMainTab = isMainTab
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