package love.moonc.androidkotlin.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.NetworkManager
import love.moonc.androidkotlin.data.RegisterRequest
import love.moonc.androidkotlin.data.UserPreferences

class AuthViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    // 这里可以定义一些 UI 状态，比如加载中、报错信息 (类似 useState)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        // 开启协程处理异步任务 (相当于 JS 的 async/await)
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // 1. 调用网络接口
                val response = NetworkManager.api.register(request)

                // 2. 根据你后端定义的结构判断是否成功 (假设 code 200 为成功)
                if (response.code == 200) {
                    // 3. 注册成功后，顺便执行登录逻辑（存本地）
                    userPreferences.saveLoginStatus(true)
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                // 处理网络错误（断网、404 等）
                errorMessage = "网络请求失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}