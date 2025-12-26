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

    // 💡 注册逻辑
    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val response = NetworkManager.api.register(request)
            if (response.code == 200) {
                val token = response.data?.token
                if (token != null) {
                    // 1. 存 Token
                    userPreferences.saveToken(token)
                    // 2. 紧接着获取并保存用户信息，完成后再跳转
                    fetchAndSaveProfile(onSuccess)
                }
            } else {
                isLoading = false
            }
        }
    }

    // 💡 登录逻辑
    fun login(request: LoginRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val response = NetworkManager.api.login(request)
            if (response.code == 200) {
                val token = response.data?.token
                if (token != null) {
                    // 1. 存 Token
                    userPreferences.saveToken(token)
                    // 2. 只有用户信息也拿到了，才算真正的“登录成功”
                    fetchAndSaveProfile(onSuccess)
                }
            } else {
                isLoading = false
            }
        }
    }

    // 💡 抽取公共方法：获取资料并存入 DataStore
    private suspend fun fetchAndSaveProfile(onSuccess: () -> Unit) {
        val profileResponse = NetworkManager.api.getProfile()
        if (profileResponse.code == 200 && profileResponse.data != null) {
            userPreferences.updateUser(profileResponse.data.user)
            onSuccess()
        }
    }
}