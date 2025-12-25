package love.moonc.androidkotlin.data

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<AuthData>
}

data class RegisterRequest(
    val nickname: String,
    val account: String,
    val password: String
)

data class AuthData(
    val token: String
)

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)