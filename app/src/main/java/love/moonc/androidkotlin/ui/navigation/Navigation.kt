package love.moonc.androidkotlin.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

object Screen {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val PROFILE = "profile"
    const val BOTTLE = "bottle"
    const val INVITE = "invite"
    const val SETTINGS = "settings"
}

data class TabItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val mainTabs = listOf(
    TabItem("首页", Icons.Default.Home, Screen.HOME),
    TabItem("发现", Icons.Default.Menu, Screen.DISCOVER),
    TabItem("我的", Icons.Default.Person, Screen.PROFILE)
)