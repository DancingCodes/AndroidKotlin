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
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = NetworkManager.api.register(request)

                // 假设你的 ApiResponse 结构里，data 包含了 token
                if (response.code == 200) {
                    val authData = response.data
                    if (authData != null) {
                        userPreferences.saveToken(authData.token)
                        onSuccess()
                    } else {
                        errorMessage = "返回数据为空"
                    }
                } else {
                    // 这里的错误会被拦截器拦截并弹 Toast，
                    // 但在这里给 errorMessage 赋值可以用于 UI 上的额外提示
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "网络请求失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}