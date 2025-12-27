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
        private set // 设为私有，防止 UI 误改状态

    fun performAuth(
        isRegister: Boolean,
        nickname: String,
        account: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (isRegister) {
            register(RegisterRequest(nickname, account, password), onSuccess)
        } else {
            login(LoginRequest(account, password), onSuccess)
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val response = NetworkManager.api.register(request)
            if (response.code == 200 && response.data?.token != null) {
                userPreferences.saveToken(response.data.token)
                fetchAndSaveProfile(onSuccess)
            } else {
                isLoading = false
            }
        }
    }

    fun login(request: LoginRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val response = NetworkManager.api.login(request)
            if (response.code == 200 && response.data?.token != null) {
                userPreferences.saveToken(response.data.token)
                fetchAndSaveProfile(onSuccess)
            } else {
                isLoading = false
            }
        }
    }

    private suspend fun fetchAndSaveProfile(onSuccess: () -> Unit) {
        val profileResponse = NetworkManager.api.getProfile()
        if (profileResponse.code == 200 && profileResponse.data != null) {
            userPreferences.updateUser(profileResponse.data.user)
            onSuccess()
        }
    }
}