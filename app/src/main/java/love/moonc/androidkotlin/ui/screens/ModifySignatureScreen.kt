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
fun ModifySignatureScreen(
    navController: NavHostController,
    // 💡 注入已有的 AuthViewModel
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val user by userPreferences.user.collectAsState(initial = null)

    var signature by remember(user) { mutableStateOf(user?.signature ?: "") }

    // 校验逻辑：内容变化且未加载中
    val isEnabled = signature != (user?.signature ?: "") &&
            signature.length <= 50 &&
            !viewModel.isLoading

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
                            // ✅ 调用 ViewModel 的更新签名方法
                            viewModel.updateSignature(signature) {
                                Toast.makeText(context, "签名已更新", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
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
                value = signature,
                onValueChange = { if (it.length <= 50) signature = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                label = { Text("个性签名") },
                placeholder = { Text("介绍一下自己吧...") },
                singleLine = false,
                maxLines = 5,
                enabled = !viewModel.isLoading, // 加载中禁用输入
                trailingIcon = {
                    if (signature.isNotEmpty() && !viewModel.isLoading) {
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