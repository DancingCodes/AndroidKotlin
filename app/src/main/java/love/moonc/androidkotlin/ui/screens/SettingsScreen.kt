package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    // 💡 用 remember 保证 userPreferences 实例稳定
    val userPreferences = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    // 💡 关键修改：initialValue 使用一个空对象或者保持 null，但不要在第一行就 return
    val user by userPreferences.user.collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        // 💡 只有当 user 确实被加载了，才显示内容
        if (user != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SectionTitle("账号安全")
                SettingsCard {
                    ProfileMenuItem(
                        icon = Icons.Default.Lock,
                        title = "修改密码",
                        onClick = { navController.navigate("modify_password") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle("操作")
                SettingsCard {
                    ProfileMenuItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "退出登录",
                        titleColor = MaterialTheme.colorScheme.error,
                        onClick = {
                            navController.navigate(Screen.AUTH) {
                                popUpTo(0) { inclusive = true }
                            }
                            scope.launch {
                                kotlinx.coroutines.delay(100)
                                userPreferences.clear()
                            }
                        }
                    )
                }
            }
        } else {
            // 💡 如果数据还没加载出来，显示一个进度条，而不是直接跳走
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }

            // 如果你确定是因为退出了才为 null，可以在这里辅助判断，但不要直接 return Scaffold
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}