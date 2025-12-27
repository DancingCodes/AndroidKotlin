package love.moonc.androidkotlin.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import love.moonc.androidkotlin.data.LoginRequest
import love.moonc.androidkotlin.data.RegisterRequest
import love.moonc.androidkotlin.data.UserPreferences
import love.moonc.androidkotlin.network.ApiService
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val api: ApiService
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

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
            try {
                // 💡 使用注入进来的 api
                val response = api.register(request)
                if (response.code == 200 && response.data?.token != null) {
                    userPreferences.saveToken(response.data.token)
                    fetchAndSaveProfile(onSuccess)
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
                val response = api.login(request)
                if (response.code == 200 && response.data?.token != null) {
                    userPreferences.saveToken(response.data.token)
                    fetchAndSaveProfile(onSuccess)
                }
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun fetchAndSaveProfile(onSuccess: () -> Unit) {
        val profileResponse = api.getProfile()
        if (profileResponse.code == 200 && profileResponse.data != null) {
            userPreferences.updateUser(profileResponse.data.user)
            onSuccess()
        }
    }
}