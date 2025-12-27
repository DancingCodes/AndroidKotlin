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
    val token by userPreferences.token.collectAsState(initial = null)
    var isInitialized by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isMainTab = mainTabs.any { it.route == currentRoute }
    LaunchedEffect(token) {
        if (isInitialized && token == null) {
            navController.navigate(Screen.Auth.route) {
                popUpTo(0) { inclusive = true }
            }
        }
        isInitialized = true
    }

    if (!isInitialized) {
        LoadingScreen()
        return
    }

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
            startDestination = if (token == null) Screen.Auth.route else Screen.Home.route,
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