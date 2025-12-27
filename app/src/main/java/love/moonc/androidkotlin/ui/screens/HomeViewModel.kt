package love.moonc.androidkotlin.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.network.ApiService // 确保导入了你的接口
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {

    var isRefreshing by mutableStateOf(false)
    var isMessageLoading by mutableStateOf(false)

    var userProfile by mutableStateOf<love.moonc.androidkotlin.data.UserProfile?>(null)

    init {
        fetchAllData()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            fetchMessage()
            kotlinx.coroutines.delay(500)
            isRefreshing = false
        }
    }

    fun fetchAllData() {
        fetchMessage()
    }

    fun fetchMessage() {
        viewModelScope.launch {
            isMessageLoading = true
            try {
                val response = api.getProfile()
                if (response.code == 200) {
                    userProfile = response.data
                }
            } catch (e: Exception) {
                // 防止网络断开等物理错误导致崩溃
                e.printStackTrace()
            } finally {
                isMessageLoading = false
            }
        }
    }
}