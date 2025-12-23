package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 头像部分
        Box(
            modifier = Modifier
                .padding(top = 64.dp)
                .size(100.dp)
                .background(MaterialTheme.colorScheme.tertiary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "头像",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }

        // 用户名
        Text(
            text = "高级开发者",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        // 签名
        Text(
            text = "不写代码就难受，代码如诗，Compose 真香",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 操作按钮
        Button(
            onClick = { /* 这里处理退出登录 */ },
            modifier = Modifier
                .fillMaxWidth(0.8f) // 宽度占屏幕的 80%
                .height(50.dp)
        ) {
            Text("退出登录")
        }
    }
}