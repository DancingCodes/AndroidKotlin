package love.moonc.androidkotlin.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val api: ApiService
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    // --- 登录注册逻辑 ---
    fun performAuth(isRegister: Boolean, nickname: String, account: String, password: String, onSuccess: () -> Unit) {
        if (isRegister) register(RegisterRequest(nickname, account, password), onSuccess)
        else login(LoginRequest(account, password), onSuccess)
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
            } catch (e: Exception) { e.printStackTrace() }
            finally { isLoading = false }
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = api.register(request)
                if (response.code == 200 && response.data?.token != null) {
                    userPreferences.saveToken(response.data.token)
                    fetchAndSaveProfile(onSuccess)
                }
            } catch (e: Exception) { e.printStackTrace() }
            finally { isLoading = false }
        }
    }

    // --- 头像上传逻辑 (新增) ---
    fun uploadAvatar(context: Context, uri: Uri) {
        viewModelScope.launch {
            isLoading = true
            try {
                val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: return@launch
                val body = MultipartBody.Part.createFormData(
                    "file", "avatar.jpg",
                    bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                val response = api.uploadAvatar(body)
                if (response.code == 200 && response.data != null) {
                    // 更新本地 DataStore 中的用户信息
//                    userPreferences.updateUserAvatar(response.data.url)
                    Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show()
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