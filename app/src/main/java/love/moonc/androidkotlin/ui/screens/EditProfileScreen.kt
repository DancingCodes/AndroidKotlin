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
import coil.request.ImageRequest
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
            // EditProfileScreen.kt 里的头像行
            EditItem(label = "头像", trailing = {
                // 💡 沿用你主页那个流光背景逻辑，保持视觉统一
                Surface(
                    modifier = Modifier.size(40.dp), // 编辑页稍微大一点点
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user?.avatar) // 💡 直接传 avatar，拼接逻辑交给拦截器或 DataStore
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }) {
                navController.navigate(Screen.Profile.Edit.Avatar.route)
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

            EditItem(label = "昵称", value = user?.nickname ?: "") {
                navController.navigate(Screen.Profile.Edit.Nickname.route)
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

            EditItem(label = "个性签名", value = user?.signature ?: "") {
                navController.navigate(Screen.Profile.Edit.Signature.route)
            }
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