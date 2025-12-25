package love.moonc.androidkotlin.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.LoginRequest
import love.moonc.androidkotlin.data.NetworkManager
import love.moonc.androidkotlin.data.RegisterRequest
import love.moonc.androidkotlin.data.UserPreferences

class AuthViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    var isLoading by mutableStateOf(false)

    // 💡 新增：用于在界面显示具体的错误信息
    var errorMessage by mutableStateOf<String?>(null)

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null // 开始请求前清空错误
            try {
                val response = NetworkManager.api.register(request)
                if (response.code == 200) {
                    response.data?.token?.let {
                        userPreferences.saveToken(it)
                        onSuccess()
                    }
                } else {
                    // 处理业务错误（如：账号已存在）
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                // ✅ 关键修复：捕获异常，防止 401/500 导致闪退
                e.printStackTrace()
                errorMessage = "注册失败：${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun login(request: LoginRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = NetworkManager.api.login(request)
                if (response.code == 200) {
                    response.data?.token?.let {
                        userPreferences.saveToken(it)
                        onSuccess()
                    }
                } else {
                    // 处理业务错误（如：密码错误）
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                // ✅ 关键修复：捕获异常，防止 401/500 导致闪退
                e.printStackTrace()
                errorMessage = "登录出错，请稍后再试"
            } finally {
                isLoading = false
            }
        }
    }
}