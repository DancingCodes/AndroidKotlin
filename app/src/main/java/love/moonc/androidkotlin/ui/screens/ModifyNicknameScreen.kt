package love.moonc.androidkotlin.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.NetworkManager
import love.moonc.androidkotlin.data.UpdateUserRequest
import love.moonc.androidkotlin.data.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyNicknameScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }

    // 💡 获取当前用户信息
    val user by userPreferences.user.collectAsState(initial = null)

    // 💡 用当前昵称初始化输入框，记得处理 null
    var nickname by remember(user) { mutableStateOf(user?.nickname ?: "") }

    // 简单的校验逻辑
    val isEnabled = nickname.isNotBlank() && nickname != user?.nickname && nickname.length <= 12

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("修改昵称") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 保存按钮：只有内容发生变化且合法时才亮起
                    TextButton(
                        enabled = isEnabled,
                        onClick = {
                            scope.launch {
                                val response = NetworkManager.api.updateProfile(
                                    UpdateUserRequest(nickname = nickname)
                                )

                                if (response.code == 200) {
                                    user?.let { userPreferences.updateUser(it.copy(nickname = nickname)) }
                                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "错误: ${response.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    ) {
                        Text(
                            "保存",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isEnabled) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nickname,
                onValueChange = { if (it.length <= 12) nickname = it }, // 限制输入长度
                modifier = Modifier.fillMaxWidth(),
                label = { Text("新昵称") },
                placeholder = { Text("请输入昵称") },
                singleLine = true,
                trailingIcon = {
                    // 如果有内容，显示一键清除按钮
                    if (nickname.isNotEmpty()) {
                        IconButton(onClick = { nickname = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "清除")
                        }
                    }
                },
                supportingText = {
                    // 显示当前字数
                    Text("${nickname.length}/12")
                },
                isError = nickname.length > 12
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "好的昵称可以让朋友们更容易记住你。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}