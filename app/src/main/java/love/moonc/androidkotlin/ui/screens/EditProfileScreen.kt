package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val user by userPreferences.user.collectAsState(initial = null)

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // 1. 头像行：点击跳转到头像修改详情页
            EditItem(label = "头像", trailing = {
                Surface(modifier = Modifier.size(40.dp), shape = CircleShape) {
                    AsyncImage(
                        model = "http://10.0.2.2:8088${user?.avatar}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }) {
                navController.navigate(Screen.MODIFY_AVATAR) // 💡 仅仅执行跳转
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

            // 2. 昵称行：点击跳转到昵称修改页
            EditItem(label = "昵称", value = user?.nickname ?: "") {
                navController.navigate(Screen.MODIFY_NICKNAME) // 💡 仅仅执行跳转
            }

            // ... 账号行 ...
        }
    }
}
@Composable
fun EditItem(
    label: String,
    value: String = "",
    showArrow: Boolean = true,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // 💡 确保 import androidx.compose.foundation.clickable
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧标签
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        // 右侧内容区：优先显示自定义组件（如头像），否则显示文本
        if (trailing != null) {
            trailing()
        } else {
            Text(
                text = value,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 最右侧箭头
        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp).padding(start = 8.dp)
            )
        }
    }
}