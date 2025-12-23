package love.moonc.androidkotlin.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    PullToRefreshBox(
        isRefreshing = homeViewModel.isRefreshing,
        onRefresh = { homeViewModel.refresh() },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // 1. 头部
            HeaderSection()

            Spacer(modifier = Modifier.height(32.dp))

            // 2. 欢迎卡片
            WelcomeCard()

            Spacer(modifier = Modifier.height(40.dp))

            // 3. 寄语卡片
            MessageCard(
                text = homeViewModel.messageText,
                isLoading = homeViewModel.isMessageLoading,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }
    }
}




@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "你好",
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "欢迎回来",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 0.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun WelcomeCard() {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(gradient, RoundedCornerShape(32.dp))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 状态标签
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "系统运行中", // 改为中文
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 文案部分
            Column {
                Text(
                    text = "你好，世界。",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "— Hello World",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 背景装饰图标
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.15f), // 稍微调亮一点点
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 30.dp, y = 30.dp)
        )
    }
}

@Composable
fun MessageCard(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "每日寄语",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            // 统一使用较圆润的风格，24.dp 或 32.dp
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .animateContentSize(), // 添加一个简单的大小变化动画
                contentAlignment = Alignment.CenterStart
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 28.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}