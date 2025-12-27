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
fun ModifySignatureScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }
    val user by userPreferences.user.collectAsState(initial = null)

    // 用当前签名初始化，如果为空则默认为空字符串
    var signature by remember(user) { mutableStateOf(user?.signature ?: "") }

    // 校验逻辑：内容变化了且不超过 50 个字即可保存（签名允许为空）
    val isEnabled = signature != (user?.signature ?: "") && signature.length <= 50

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("修改个性签名") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(
                        enabled = isEnabled,
                        onClick = {
                            scope.launch {
                                val response = NetworkManager.api.updateProfile(
                                    UpdateUserRequest(signature = signature)
                                )
                                if (response.code == 200) {
                                    user?.let { userPreferences.updateUser(it.copy(signature = signature)) }
                                    Toast.makeText(context, "签名已更新", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
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
                value = signature,
                onValueChange = { if (it.length <= 50) signature = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp), // 💡 设置最小高度，看起来像个大输入框
                label = { Text("个性签名") },
                placeholder = { Text("介绍一下自己吧...") },
                singleLine = false, // 💡 允许多行输入
                maxLines = 5,
                trailingIcon = {
                    if (signature.isNotEmpty()) {
                        IconButton(onClick = { signature = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "清除")
                        }
                    }
                },
                supportingText = {
                    Text("${signature.length}/50")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "有趣的签名可以让你交到更多志同道合的朋友。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}