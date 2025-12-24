package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun BottleScreen(navController: NavController) {
    var message by remember { mutableStateOf("") }

    // 定义海洋色系
    val oceanLight = Color(0xFFE0F2F1) // 极浅的青蓝色（水面感）
    val oceanDeep = Color(0xFFB2EBF2)  // 稍微深一点的湖蓝

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color.White, oceanLight, oceanDeep))
            ) // 营造水面向下的透视感
    ) {
        // --- 背景装饰：底部的装饰性“波浪” ---
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(200.dp),
            color = Color(0x334FC3F7), // 极淡的蓝色遮罩
            shape = RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp) // 模拟一个大弧度的波浪
        ) {}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 1. 顶部栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "海边的信箱",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Light,
                        letterSpacing = 2.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            // 2. 核心：像“海滩卵石”一样圆润的磨砂信纸
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(32.dp),      // 1. 更圆润的弧度，像鹅卵石一样
                color = Color.White.copy(alpha = 0.6f), // 2. 更有通透感的半透明
                border = BorderStroke(1.5.dp, Color.White), // 3. 稍微加粗白色边框，强化边缘质感
            ) {
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = {
                        Text("将秘密托付给洋流...", color = Color.Gray.copy(alpha = 0.5f))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp)
                        .padding(16.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF00ACC1)
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // 顶开底部按钮

            // 3. 底部按钮：使用更有“浪花”感的配色
            Button(
                onClick = { if (message.isNotBlank()) navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 40.dp)
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00ACC1), // 更有活力的海蓝色
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text("扔向大海", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}