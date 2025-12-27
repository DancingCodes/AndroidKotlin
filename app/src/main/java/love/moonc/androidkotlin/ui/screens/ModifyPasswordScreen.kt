package love.moonc.androidkotlin.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.NetworkManager
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import love.moonc.androidkotlin.data.UpdateUserRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 控制密码可见性
    var passwordVisible by remember { mutableStateOf(false) }

    // 校验逻辑：长度需 >= 6 且 两次输入必须一致
    val isLengthOk = password.length >= 6
    val isMatch = password == confirmPassword && confirmPassword.isNotEmpty()
    val canSave = isLengthOk && isMatch

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("修改密码") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
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
                .padding(20.dp)
        ) {
            Text(
                text = "设置新密码",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "建议使用字母、数字的组合，长度至少6位。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- 新密码输入框 ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("新密码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 确认密码输入框 ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("确认新密码") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(), // 确认框通常强制隐藏
                isError = confirmPassword.isNotEmpty() && !isMatch,
                supportingText = {
                    if (confirmPassword.isNotEmpty() && !isMatch) {
                        Text("两次输入的密码不一致", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 提交按钮 ---
            Button(
                onClick = {
                    scope.launch {
                        val response = NetworkManager.api.updateProfile(
                            UpdateUserRequest(password = password)
                        )
                        if (response.code == 200) {
                            Toast.makeText(context, "密码更新成功", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("确认修改")
            }
        }
    }
}