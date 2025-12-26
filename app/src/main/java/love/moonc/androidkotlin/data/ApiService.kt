package love.moonc.androidkotlin.data

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<AuthData>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<AuthData>

    @GET("/u/profile")
    suspend fun getProfile(): BaseResponse<UserResponse>


    @POST("/u/update")
    suspend fun updateProfile(@Body request: UpdateRequest): BaseResponse<String>


    @Multipart
    @POST("/common/upload")
    suspend fun uploadAvatar(
        @Part file: MultipartBody.Part
    ): BaseResponse<UploadResponse>
}

data class RegisterRequest(
    val nickname: String,
    val account: String,
    val password: String
)

data class LoginRequest(
    val account: String,
    val password: String
)


data class AuthData(
    val token: String
)

data class UserResponse(
    val user: User
)

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)

data class UploadResponse(
    val url: String
)

data class UpdateRequest(
    val password: String? = null,
    val nickname: String? = null,
    val avatar: String? = null,
    val signature: String? = null
)