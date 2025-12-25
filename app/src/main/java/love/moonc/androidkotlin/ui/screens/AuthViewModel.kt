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
    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = NetworkManager.api.register(request)

                // 假设你的 ApiResponse 结构里，data 包含了 token
                if (response.code == 200) {
                    val authData = response.data
                    if (authData != null) {
                        userPreferences.saveToken(authData.token)
                        onSuccess()
                    }
                }
            } finally {
                isLoading = false
            }
        }

    }

    fun login(request: LoginRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = NetworkManager.api.login(request)
                if (response.code == 200) {
                    val authData = response.data
                    if (authData != null) {
                        userPreferences.saveToken(authData.token)
                        onSuccess()
                    }
                }
            } finally {
                isLoading = false
            }
        }

    }
}