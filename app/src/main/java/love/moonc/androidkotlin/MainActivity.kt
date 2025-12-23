package love.moonc.androidkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import love.moonc.androidkotlin.ui.components.TabBar
import love.moonc.androidkotlin.ui.components.TabItem
import love.moonc.androidkotlin.ui.screens.HomeScreen
import love.moonc.androidkotlin.ui.screens.MessageScreen
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

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem("首页", Icons.Default.Home),
        TabItem("消息", Icons.Default.Notifications),
        TabItem("我的", Icons.Default.Person)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TabBar(
                items = tabs,
                selectedIndex = selectedIndex,
                onTabSelected = { index -> selectedIndex = index }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AnimatedContent(
                targetState = selectedIndex,
                transitionSpec = {
                    fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                }
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> HomeScreen()
                    1 -> MessageScreen()
                    2 -> ProfileScreen()
                }
            }
        }
    }
}