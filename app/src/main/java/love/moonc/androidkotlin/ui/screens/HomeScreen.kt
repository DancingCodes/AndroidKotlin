package love.moonc.androidkotlin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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