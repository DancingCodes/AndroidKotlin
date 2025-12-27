package love.moonc.androidkotlin.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    // 登录注册
    object Auth : Screen("auth")

    // 首页
    object Home : Screen("home")

    // 发现
    object Discover : Screen("discover") {
        object Bottle : Screen("discover/bottle")
    }

    // 我的
    object Profile : Screen("profile") {
        // 邀请好友
        object Invite : Screen("profile/invite")
        // 系统设置
        object Settings : Screen("profile/settings")

        // 编辑用户
        object Edit : Screen("profile/edit") {
            // 编辑用户名
            object Nickname : Screen("profile/edit/nickname")
            // 编辑个性签名
            object Signature : Screen("profile/edit/signature")
            // 编辑密码
            object Password : Screen("profile/edit/password")
            // 编辑头像
            object Avatar : Screen("profile/edit/avatar")
        }
    }
}

data class TabItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val mainTabs = listOf(
    TabItem("首页", Icons.Default.Home, Screen.Home.route),
    TabItem("发现", Icons.Default.Search, Screen.Discover.route),
    TabItem("我的", Icons.Default.Person, Screen.Profile.route)
)