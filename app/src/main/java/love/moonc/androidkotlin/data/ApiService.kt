package love.moonc.androidkotlin.data

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<String>
}

// 对应 ApiFox 的数据模型
data class RegisterRequest(
    val username: String,
    val account: String,
    val password: String
)

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)