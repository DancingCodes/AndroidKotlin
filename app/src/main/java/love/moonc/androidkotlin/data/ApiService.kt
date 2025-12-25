package love.moonc.androidkotlin.data

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<String>
}

data class RegisterRequest(
    val nickname: String,
    val account: String,
    val password: String
)

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)