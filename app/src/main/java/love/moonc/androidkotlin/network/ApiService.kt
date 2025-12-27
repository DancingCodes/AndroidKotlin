package love.moonc.androidkotlin.network

import love.moonc.androidkotlin.data.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse<AuthData>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<AuthData>

    @GET("u/profile")
    suspend fun getProfile(): BaseResponse<UserProfile>

    @POST("u/update")
    suspend fun updateProfile(@Body request: UpdateUserRequest): BaseResponse<String>

    @Multipart
    @POST("common/upload")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<UploadResponse>
}