package love.moonc.androidkotlin.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import love.moonc.androidkotlin.data.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyNicknameScreen(
    navController: NavHostController,
    // 💡 注入已有的 AuthViewModel
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    // 获取当前用户信息
    val user by userPreferences.user.collectAsState(initial = null)

    // 用当前昵称初始化输入框
    var nickname by remember(user) { mutableStateOf(user?.nickname ?: "") }

    // 校验逻辑：内容变化、不为空、长度合法且当前没有正在加载
    val isEnabled = nickname.isNotBlank() &&
            nickname != user?.nickname &&
            nickname.length <= 12 &&
            !viewModel.isLoading

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
                    TextButton(
                        enabled = isEnabled,
                        onClick = {
                            // ✅ 调用 AuthViewModel 里的 updateNickname 方法
                            // ✅ 逻辑已经在 ViewModel 里写好了，包括调用 api.updateProfile 和 fetchAndSaveProfile
                            viewModel.updateNickname(nickname) {
                                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                progress = { 0.5f }, // 改用 Lambda 形式
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                "保存",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isEnabled) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
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
                onValueChange = { if (it.length <= 12) nickname = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("新昵称") },
                placeholder = { Text("请输入昵称") },
                singleLine = true,
                enabled = !viewModel.isLoading,
                trailingIcon = {
                    if (nickname.isNotEmpty() && !viewModel.isLoading) {
                        IconButton(onClick = { nickname = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "清除")
                        }
                    }
                },
                supportingText = {
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