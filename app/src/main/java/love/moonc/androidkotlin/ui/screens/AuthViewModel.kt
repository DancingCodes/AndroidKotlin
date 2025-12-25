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
                if (response.code == 200) {
                    userPreferences.saveLoginStatus(true)
                    onSuccess()
                } else {
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