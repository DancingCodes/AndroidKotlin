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

    @GET("u/profile")
    suspend fun getProfile(): BaseResponse<UserProfile>

    @POST("u/update")
    suspend fun updateProfile(@Body request: UpdateUserRequest): BaseResponse<String>

    @Multipart
    @POST("common/upload")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<UploadResponse>
}