package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import love.moonc.androidkotlin.ui.navigation.Screen

@Composable
fun DiscoverScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // 页面标题
        Text(
            text = "发现",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "遇见有趣的灵魂和故事",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 功能网格
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DiscoveryCard(
                    title = "漂流瓶",
                    subtitle = "投递你的心事",
                    icon = Icons.Default.Email, // 邮件图标代替
                    containerColor = Color(0xFFE3F2FD),
                    contentColor = Color(0xFF1976D2)
                ) {
                    navController.navigate(Screen.Discover.Bottle.route)
                }
            }
            item {
                DiscoveryCard(
                    title = "深夜广场",
                    subtitle = "搜索共鸣的声音",
                    icon = Icons.Default.Search, // 搜索图标代替（寓意寻找）
                    containerColor = Color(0xFFEDE7F6),
                    contentColor = Color(0xFF673AB7)
                ) { }
            }
            item {
                DiscoveryCard(
                    title = "分享足迹",
                    subtitle = "记录此刻美好",
                    icon = Icons.Default.Share, // 分享图标
                    containerColor = Color(0xFFE8F5E9), // 浅绿色
                    contentColor = Color(0xFF2E7D32)
                ) { }
            }
            item {
                DiscoveryCard(
                    title = "心动匹配",
                    subtitle = "遇见对的人",
                    icon = Icons.Default.Favorite, // 爱心图标
                    containerColor = Color(0xFFFCE4EC),
                    contentColor = Color(0xFFC2185B)
                ) { }
            }
        }
    }
}

@Composable
fun DiscoveryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}