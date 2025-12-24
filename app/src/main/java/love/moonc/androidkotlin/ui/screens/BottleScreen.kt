package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleScreen(navController: NavController) {
    var message by remember { mutableStateOf("") }

    // 使用渐变蓝色模拟海洋背景
    val oceanGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF039BE5), // 浅蓝
            Color(0xFF01579B)  // 深蓝
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("漂流瓶", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // 使用已导入的 AutoMirrored 版本
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                // 设置 TopAppBar 背景透明或蓝色，使其与海洋背景融合
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF039BE5)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(oceanGradient) // 背景铺满全屏
                .padding(padding)          // 避开状态栏和导航栏
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 信纸输入框
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.9f),
                shadowElevation = 4.dp
            ) {
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("写下你的心里话，扔进大海吧...") },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF0277BD)
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 扔进海里按钮
            Button(
                onClick = {
                    if (message.isNotBlank()) {
                        /* 这里可以添加发送成功的提示 */
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f) // 宽度稍微收窄一点更好看
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA000), // 橙色在蓝色背景上很醒目
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("扔进大海", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}